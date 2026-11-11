package com.esidhu.portfolio_backend.service;

import static com.esidhu.portfolio_backend.constant.Constant.GITHUB_API_URL;
import static com.esidhu.portfolio_backend.constant.Constant.GITHUB_USERNAME;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GitService {
    private final RestClient restClient;

    @Value("${git.api.pat}")
    private String gitApiPat;

    public GitService() {
        this.restClient = RestClient.create();
    }

    private String makeRequest(String url) {
        try {

            String response = restClient.get().uri(url).headers(headers -> {
                headers.set("Authorization", "Bearer " + gitApiPat);
                headers.setContentType(MediaType.APPLICATION_JSON);
            }).retrieve().body(String.class);

            return response;
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
