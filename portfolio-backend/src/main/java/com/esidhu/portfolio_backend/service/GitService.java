package com.esidhu.portfolio_backend.service;

import static com.esidhu.portfolio_backend.constant.Constant.GITHUB_API_URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.esidhu.portfolio_backend.model.Etag;
import com.esidhu.portfolio_backend.model.Project;
import com.esidhu.portfolio_backend.repo.EtagRepo;

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

  private <T> T makeRequest(String url, ParameterizedTypeReference<T> responseType) {
    try {
      Optional<Etag> etagOptional = etagRepo.findByUrl(url);
      Optional<String> etagValue = etagOptional.map(Etag::getValue);

      ResponseEntity<T> response = restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
          .headers(headers -> {
            etagValue.ifPresent(value -> headers.set("If-None-Match", value));
            headers.setBearerAuth(gitApiPat);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
          })
          .retrieve().toEntity(responseType);

      // Save etag value if it changed (or does not exist)
      if (response.getStatusCode() != HttpStatus.NOT_MODIFIED) {
        Etag etag = etagOptional.orElse(new Etag(url));
        etag.setValue(response.getHeaders().getETag());
        etagRepo.save(etag);
      }

      return response.getBody();
    } catch (Exception exception) {
      throw new RuntimeException(exception.getMessage());
    }
  }

  public @Nullable List<Project> getAllRepos() {
    // TODO: Change to request pages
    String url = "%s/user/repos".formatted(GITHUB_API_URL);
    return makeRequest(url, new ParameterizedTypeReference<List<Project>>() {
    });
  }

  public @Nullable Project getRepo(Long id) {
    String url = "%s/repositories/%d".formatted(GITHUB_API_URL, id);
    return makeRequest(url, ParameterizedTypeReference.forType(Project.class));
  }

  public @Nullable Project getRepoReadme(Long id) {
    String url = "%s/repositories/%d/contents/README.md".formatted(GITHUB_API_URL, id);
    return makeRequest(url, ParameterizedTypeReference.forType(Project.class));
  }

  public @Nullable Project getRepoLatestCommitDate(Long id) {
    String url = "%s/repositories/%d/commits?sha=main&per_page=1".formatted(GITHUB_API_URL, id);
    List<Project> commits = makeRequest(url, new ParameterizedTypeReference<List<Project>>() {
    });
    // Either return Project containing date, or null
    return (commits != null && !commits.isEmpty()) ? commits.get(0) : null;
  }
}
