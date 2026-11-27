package com.esidhu.portfolio_backend.service;

import static com.esidhu.portfolio_backend.constant.Constant.GITHUB_API_URL;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import com.esidhu.portfolio_backend.dto.ProjectDto;
import com.esidhu.portfolio_backend.model.Etag;
import com.esidhu.portfolio_backend.repo.EtagRepo;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

@Service
public class GitService {
  private final RestClient restClient;
  private final EtagRepo etagRepo;

  @Value("${git.api.pat}")
  private String gitApiPat;

  public GitService(EtagRepo etagRepo) {
    this.restClient = RestClient.create();
    this.etagRepo = etagRepo;
  }

  public List<ProjectDto> getAllRepos(int page, int size) {
    List<ProjectDto> projects = new ArrayList<>();

    String url = UriComponentsBuilder.fromUriString(GITHUB_API_URL)
        .path("/user/repos")
        .queryParam("per_page", size)
        .queryParam("page", page)
        .toUriString();

    Pair<JsonNode, HttpStatusCode> result = fetchData(List.of(url), true).get(0);
    JsonNode body = result.getFirst();
    if (body.isNull() || !body.isArray()) {
      return Collections.emptyList();
    }

    for (JsonNode repository : body) {
      projects.add(toProject(repository));
    }

    return projects;
  }

  public List<ProjectDto> getListOfRepos(List<Long> idList) {
    if (idList == null || idList.isEmpty()) {
      return Collections.emptyList();
    }

    List<String> urls = idList.stream()
        .map(id -> UriComponentsBuilder.fromUriString(GITHUB_API_URL)
            .path("/repositories/{id}")
            .buildAndExpand(id)
            .toUriString())
        .toList();

    List<ProjectDto> projects = fetchData(urls, true).stream()
        .map(Pair::getFirst)
        .filter(body -> body != null && !body.isNull() && body.isObject())
        .map(this::toProject)
        .toList();

    return projects;
  }

  public Pair<Optional<ProjectDto>, HttpStatusCode> getRepo(Long id) {
    String url = UriComponentsBuilder.fromUriString(GITHUB_API_URL)
        .path("/repositories/{id}")
        .buildAndExpand(id)
        .toUriString();

    Pair<JsonNode, HttpStatusCode> result = fetchData(List.of(url), true).get(0);
    JsonNode body = result.getFirst();
    return Pair.of(Optional.ofNullable(body != null && !body.isNull() ? toProject(body) : null), result.getSecond());
  }

  private ProjectDto toProject(JsonNode repo) {
    ProjectDto project = new ProjectDto();

    JsonNode id = repo.get("id");
    if (id.isNull() || !id.isNumber())
      throw new RuntimeException("invalid repository id recieved");
    project.setId(id.longValue());

    project.setName(textOrNull(repo.get("name")));
    project.setDescription(textOrNull(repo.get("description")));
    JsonNode owner = repo.get("owner");
    project.setOwner(owner != null ? textOrNull(owner.get("login")) : null);

    return project;
  }

  private String textOrNull(JsonNode value) {
    return value != null && !value.isNull() ? value.asString() : null;
  }

  public @Nullable LocalDate getRepoLatestCommitDate(Long id) {

    String url = UriComponentsBuilder.fromUriString(GITHUB_API_URL)
        .path("/repositories/{id}/commits")
        .queryParam("per_page", 1)
        .buildAndExpand(id)
        .toUriString();

    Pair<JsonNode, HttpStatusCode> result = fetchData(List.of(url), false).get(0);
    // Parse commit date as Instant after extracting from response body
    JsonNode body = result.getFirst();
    String dateString = !body.isNull() && body.isArray() && body.size() > 0
        ? body.get(0).path("commit").path("author").path("date").asString()
        : null;
    LocalDate latestCommitDate = dateString != null && !dateString.isBlank()
        ? Instant.parse(dateString.strip()).atZone(ZoneOffset.UTC).toLocalDate()
        : null;

    // No need to check for 304 status externally as it will only ever be null if
    // there are no commits. So, if this is null the database value will not need to
    // be changed either way
    return latestCommitDate;
  }

  // If given a single url makes a single request normally
  // If given multiple urls makes all the separate requests in parallel
  public List<Pair<JsonNode, HttpStatusCode>> fetchData(List<String> urls, boolean suppressNotFound) {
    if (urls == null || urls.isEmpty()) {
      return List.of();
    }

    BiFunction<List<String>, Boolean, List<Pair<JsonNode, HttpStatusCode>>> strategy = (urls.size() == 1)
        ? this::makeSingleRequest
        : this::makeParallelRequests;

    return strategy.apply(urls, suppressNotFound);
  }

  private List<Pair<JsonNode, HttpStatusCode>> makeSingleRequest(List<String> urls, boolean suppressNotFound) {
    return List.of(makeRequest(urls.get(0), suppressNotFound));
  }

  private List<Pair<JsonNode, HttpStatusCode>> makeParallelRequests(List<String> urls, boolean suppressNotFound) {
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      List<CompletableFuture<Pair<JsonNode, HttpStatusCode>>> futures = urls.stream()
          .map(url -> CompletableFuture.supplyAsync(
              () -> makeRequest(url, suppressNotFound),
              executor))
          .toList();

      return futures.stream()
          .map(CompletableFuture::join)
          .toList();
    }
  }

  private Pair<JsonNode, HttpStatusCode> makeRequest(String url, boolean suppressNotFound) {
    try {
      Optional<Etag> etagOptional = etagRepo.findById(url);
      Optional<String> etagValue = etagOptional.map(Etag::getValue);

      ResponseEntity<JsonNode> response = restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
          .headers(headers -> {
            etagValue.ifPresent(value -> headers.set("If-None-Match", value));
            headers.setBearerAuth(gitApiPat);
            headers.set("User-Agent", "Spring-Boot-App");
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
          }).retrieve()
          .onStatus(status -> status.value() == 404 && suppressNotFound, (_, _) -> {
          }).onStatus(status -> status.value() == 409, (_, _) -> {
          }).toEntity(JsonNode.class);

      if (response.getStatusCode() != HttpStatus.NOT_MODIFIED) {
        String newEtag = response.getHeaders().getETag();
        if (newEtag != null) {
          etagRepo.upsertEtag(url, newEtag);
        }
      }

      JsonNode body = response.getBody();
      return Pair.of(body != null ? body : JsonNodeFactory.instance.nullNode(), response.getStatusCode());
    } catch (RestClientResponseException ex) {
      System.err.println("Failed URL: " + url);
      System.err.println("Status: " + ex.getStatusCode() + " | Body: " + ex.getResponseBodyAsString());
      throw new RuntimeException(ex.getMessage(), ex);
    }
  }
}
