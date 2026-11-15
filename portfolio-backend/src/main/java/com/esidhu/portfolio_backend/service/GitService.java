package com.esidhu.portfolio_backend.service;

import static com.esidhu.portfolio_backend.constant.Constant.GITHUB_API_URL;
import static com.esidhu.portfolio_backend.constant.Constant.GITHUB_USERNAME;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.esidhu.portfolio_backend.model.Etag;
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

  private String makeRequest(String url) {
    try {
      Optional<Etag> etagOptional = etagRepo.findByUrl(url);
      Optional<String> etagValue = etagOptional.map(Etag::getValue);

      ResponseEntity<String> response = restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
          .headers(headers -> {
            etagValue.ifPresent(value -> headers.set("If-None-Match", value));
            headers.setBearerAuth(gitApiPat);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
          }).retrieve().toEntity(String.class);

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

  public String getAllRepos() {
    String url = "%s/user/repos".formatted(GITHUB_API_URL);
    return makeRequest(url);
  }

  public String getRepoByName(String name) {
    String url = "%s/repos/%s/%s".formatted(GITHUB_API_URL, GITHUB_USERNAME, name);
    return makeRequest(url);
  }

  public String getRepoReadme(String name) {
    String url = "%s/repos/%s/%s/contents/README.md".formatted(GITHUB_API_URL, GITHUB_USERNAME, name);
    return makeRequest(url);
  }

  public String getRepoLatestCommitDate(String name) {
    String url = "%s/repos/%s/%s/commits?sha=main&per_page=1".formatted(GITHUB_API_URL, GITHUB_USERNAME, name);
    return makeRequest(url);
  }
}
