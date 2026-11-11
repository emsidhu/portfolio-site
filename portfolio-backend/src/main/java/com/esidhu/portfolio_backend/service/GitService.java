package com.esidhu.portfolio_backend.service;

import static com.esidhu.portfolio_backend.constant.Constant.GITHUB_API_URL;
import static com.esidhu.portfolio_backend.constant.Constant.GITHUB_USERNAME;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitService {
    private final RestTemplate restTemplate;

    @Value("${git.api.pat}")
    private String gitApiPat;

    public GitService() {
        this.restTemplate = new RestTemplate();
    }

    private String makeRequest(String url) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + gitApiPat);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

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

    public String getRepoLatestCommit(String name) {
        String url = "%s/repos/%s/%s/commits?sha=main&per_page=1".formatted(GITHUB_API_URL, GITHUB_USERNAME, name);
        return makeRequest(url);
    }
}
