package com.inboxreader.backend.service.impl;

import com.inboxreader.backend.config.GoogleOAuthConfig;
import com.inboxreader.backend.entity.GmailCredential;
import com.inboxreader.backend.repository.GmailCredentialRepository;
import com.inboxreader.backend.service.GmailAuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GmailAuthServiceImpl implements GmailAuthService {

    private final GoogleOAuthConfig oAuthConfig;
    private final GmailCredentialRepository credentialRepository;
    private final RestTemplate restTemplate;

    @Override
    public String buildAuthUrl() {
        return UriComponentsBuilder.fromHttpUrl(GoogleOAuthConfig.AUTH_ENDPOINT)
                .queryParam("client_id", oAuthConfig.getClientId())
                .queryParam("redirect_uri", oAuthConfig.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", GoogleOAuthConfig.GMAIL_READONLY_SCOPE)
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .build()
                .toUriString();
    }

    @Override
    public void handleCallback(String code, Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", oAuthConfig.getClientId());
        body.add("client_secret", oAuthConfig.getClientSecret());
        body.add("redirect_uri", oAuthConfig.getRedirectUri());
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        Map<String, Object> response = restTemplate.postForObject(
                GoogleOAuthConfig.TOKEN_ENDPOINT, request, Map.class);

        String accessToken = (String) response.get("access_token");
        String refreshToken = (String) response.get("refresh_token");
        Integer expiresIn = (Integer) response.get("expires_in");
        String scope = (String) response.get("scope");

        GmailCredential credential = credentialRepository.findByUserId(userId)
                .orElse(new GmailCredential(null, userId, accessToken, refreshToken,
                        LocalDateTime.now().plusSeconds(expiresIn), scope, LocalDateTime.now()));

        credential.setAccessToken(accessToken);
        credential.setTokenExpiry(LocalDateTime.now().plusSeconds(expiresIn));
        if (refreshToken != null) {
            credential.setRefreshToken(refreshToken);
        }

        credentialRepository.save(credential);
    }

    @Override
    public String getValidAccessToken(Long userId) {
        GmailCredential credential = credentialRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("No Gmail credential found for user " + userId));

        if (credential.getTokenExpiry().isAfter(LocalDateTime.now().plusMinutes(2))) {
            return credential.getAccessToken();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("refresh_token", credential.getRefreshToken());
        body.add("client_id", oAuthConfig.getClientId());
        body.add("client_secret", oAuthConfig.getClientSecret());
        body.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        Map<String, Object> response = restTemplate.postForObject(
                GoogleOAuthConfig.TOKEN_ENDPOINT, request, Map.class);

        String newAccessToken = (String) response.get("access_token");
        Integer expiresIn = (Integer) response.get("expires_in");

        credential.setAccessToken(newAccessToken);
        credential.setTokenExpiry(LocalDateTime.now().plusSeconds(expiresIn));
        credentialRepository.save(credential);

        return newAccessToken;
    }
    @Override
public boolean isConnected(Long userId) {
    return credentialRepository.findByUserId(userId).isPresent();
}

@Override
public void disconnect(Long userId) {
    credentialRepository.deleteByUserId(userId);
}   
}
