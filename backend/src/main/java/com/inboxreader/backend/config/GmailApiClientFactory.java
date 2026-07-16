package com.inboxreader.backend.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.io.IOException;

@Component
public class GmailApiClientFactory {

    public Gmail build(String accessToken) throws GeneralSecurityException, IOException {
        HttpRequestInitializer requestInitializer = request ->
                request.getHeaders().setAuthorization("Bearer " + accessToken);

        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer)
                .setApplicationName("Inbox Reader")
                .build();
    }
}