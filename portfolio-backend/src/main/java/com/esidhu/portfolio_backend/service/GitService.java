package com.esidhu.portfolio_backend.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitService {
    private final RestTemplate restTemplate;
    private static final String GITHUB_API_URL = "https://api.github.com";

    @Value("${git.api.pat}")
    private String gitApiPat;

    public GitService() {
        this.restTemplate = new RestTemplate();
    }

    public String getRepositoryList() {
        try {
            String url = GITHUB_API_URL + "/user/repos";

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
}
