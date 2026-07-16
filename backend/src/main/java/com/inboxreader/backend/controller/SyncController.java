package com.inboxreader.backend.controller;

import com.inboxreader.backend.service.GmailSyncService;
import com.inboxreader.backend.service.SyncMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
public class SyncController {

    private static final Long CURRENT_USER_ID = 1L; // single-user for now

    private final GmailSyncService gmailSyncService;
    private final SyncMetadataService syncMetadataService;

    @PostMapping("/trigger")
    public ResponseEntity<SyncResult> trigger() {
        try {
            int imported = gmailSyncService.syncNewsletters(CURRENT_USER_ID);
            syncMetadataService.recordSuccess(CURRENT_USER_ID, imported);
            return ResponseEntity.ok(new SyncResult(true, imported, null));
        } catch (Exception e) {
            syncMetadataService.recordFailure(CURRENT_USER_ID, e.getMessage());
            return ResponseEntity.status(500).body(new SyncResult(false, 0, e.getMessage()));
        }
    }

    private record SyncResult(boolean success, int articlesImported, String error) {}
}