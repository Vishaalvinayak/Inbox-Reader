package com.inboxreader.backend.controller;

import com.inboxreader.backend.service.GmailAuthService;
import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth/google")
@RequiredArgsConstructor
public class GmailAuthController {

    private static final Long CURRENT_USER_ID = 1L; // single-user for now

    private final GmailAuthService gmailAuthService;

    @GetMapping("/connect")
    public ResponseEntity<Void> connect() {
        String authUrl = gmailAuthService.buildAuthUrl();
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(authUrl))
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<Void> callback(@RequestParam("code") String code) {
        gmailAuthService.handleCallback(code, CURRENT_USER_ID);

        // Redirect back to the frontend once tokens are stored
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:5173/settings?gmail=connected"))
                .build();
    }

    @GetMapping("/status")
    public ResponseEntity<GmailConnectionStatus> status() {
        boolean connected = gmailAuthService.isConnected(CURRENT_USER_ID);
        return ResponseEntity.ok(new GmailConnectionStatus(connected));
    }

    @DeleteMapping("/disconnect")
    public ResponseEntity<Void> disconnect() {
        gmailAuthService.disconnect(CURRENT_USER_ID);
        return ResponseEntity.noContent().build();
    }

    private record GmailConnectionStatus(boolean connected) {}
}