package com.esidhu.portfolio_backend.service;

import static com.esidhu.portfolio_backend.constant.Constant.GITHUB_API_URL;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.util.Pair;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import com.esidhu.portfolio_backend.model.Etag;
import com.esidhu.portfolio_backend.model.Project;
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

      // Save etag value if it changed (or does not exist)
      if (response.getStatusCode() != HttpStatus.NOT_MODIFIED) {
        String newEtag = response.getHeaders().getETag();
        if (newEtag != null) {
          int updatedRows = etagRepo.updateValueByUrl(url, newEtag);
          if (updatedRows == 0) {
            try {
              etagRepo.save(new Etag(url, newEtag));
            } catch (DataIntegrityViolationException ignored) {
              etagRepo.updateValueByUrl(url, newEtag);
            }
          }
        }
      }

      JsonNode body = response.getBody();
      return Pair.of(body != null ? body : JsonNodeFactory.instance.nullNode(), response.getStatusCode());
    } catch (RestClientResponseException ex) {
      // Logs the exact status, response JSON from GitHub, and headers
      System.err.println("Failed URL: " + url);
      System.err.println("Status: " + ex.getStatusCode() + " | Body: " + ex.getResponseBodyAsString());
      throw new RuntimeException(ex.getMessage(), ex);
    }
  }

  public @Nullable Pair<List<Project>, HttpStatusCode> getAllRepos() {
    String url = UriComponentsBuilder.fromUriString(GITHUB_API_URL)
        .path("/user/repos")
        .toUriString();

    Pair<JsonNode, HttpStatusCode> result = makeRequest(url, false);
    JsonNode body = result.getFirst();
    if (body == null || !body.isArray()) {
      return Pair.of(Collections.emptyList(), result.getSecond());
    }

    // Convert Json to list of projects
    List<Project> projects = new ArrayList<>();
    for (JsonNode repository : body) {
      projects.add(toProject(repository));
    }
    return Pair.of(projects, result.getSecond());
  }

  public Pair<Optional<Project>, HttpStatusCode> getRepo(Long id) {
    String url = UriComponentsBuilder.fromUriString(GITHUB_API_URL)
        .path("/repositories/{id}")
        .buildAndExpand(id)
        .toUriString();

    Pair<JsonNode, HttpStatusCode> result = makeRequest(url, false);
    JsonNode body = result.getFirst();
    return Pair.of(Optional.ofNullable(body != null && !body.isNull() ? toProject(body) : null), result.getSecond());
  }

  private Project toProject(JsonNode repository) {
    Project project = new Project();
    JsonNode id = repository.get("id");
    if (id == null || !id.isNumber())
      throw new RuntimeException("invalid repository id recieved");
    project.setId(id.longValue());

    project.setName(textOrNull(repository.get("name")));
    project.setDescription(textOrNull(repository.get("description")));

    JsonNode owner = repository.get("owner");
    project.setOwner(owner != null ? textOrNull(owner.get("login")) : null);
    return project;
  }

  private String textOrNull(JsonNode value) {
    return value != null && !value.isNull() ? value.asString() : null;
  }

  public @Nullable Pair<String, HttpStatusCode> getRepoReadMe(Long id) {
    String url = UriComponentsBuilder.fromUriString(GITHUB_API_URL)
        .path("/repositories/{id}/readme")
        .buildAndExpand(id)
        .toUriString();

    Pair<JsonNode, HttpStatusCode> result = makeRequest(url, true);
    // Get README content from body and decode into plaintext
    JsonNode body = result.getFirst();
    JsonNode content = body != null ? body.get("content") : null;
    String base64Content = content != null && !content.isNull() ? content.asString() : null;
    String plainTextContent = base64Content != null
        ? new String(Base64.getMimeDecoder().decode(base64Content), StandardCharsets.UTF_8)
        : "";

    return Pair.of(plainTextContent, result.getSecond());
  }

  public @Nullable Instant getRepoLatestCommitDate(Long id) {

    String url = UriComponentsBuilder.fromUriString(GITHUB_API_URL)
        .path("/repositories/{id}/commits")
        .queryParam("per_page", 1)
        .buildAndExpand(id)
        .toUriString();

    Pair<JsonNode, HttpStatusCode> result = makeRequest(url, false);
    // Parse commit date as Instant after extracting from response body
    JsonNode body = result.getFirst();
    String dateString = body != null && body.isArray() && body.size() > 0
        ? body.get(0).path("commit").path("author").path("date").asString()
        : null;
    Instant latestCommitDate = dateString != null && !dateString.isBlank()
        ? Instant.parse(dateString)
        : null;

    // No need to check for 304 status externally as it will only ever be null if
    // there are no commits. So, if this is null the database value will not need to
    // be changed either way
    return latestCommitDate;
  }
}
