package org.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TokenBlacklistService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${auth.service.url}")
    private String AUTH_URL;

    public boolean isTokenInBlacklist(String token) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(AUTH_URL + "/blacklist")
                .queryParam("token", token);
        String response = restTemplate.getForObject(builder.toUriString(), String.class);
        return Boolean.valueOf(response);
    }

}
