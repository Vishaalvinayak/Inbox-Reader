package com.inboxreader.backend.service;

public interface GmailAuthService {
    String buildAuthUrl();
    void handleCallback(String code, Long userId);
    String getValidAccessToken(Long userId);
    boolean isConnected(Long userId);
    void disconnect(Long userId);
}
