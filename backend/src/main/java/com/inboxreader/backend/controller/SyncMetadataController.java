package com.inboxreader.backend.controller;

import com.inboxreader.backend.dto.response.SyncStatusResponse;
import com.inboxreader.backend.service.SyncMetadataService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sync-status")
public class SyncMetadataController {

    private final SyncMetadataService syncMetadataService;

    public SyncMetadataController(SyncMetadataService syncMetadataService) {
        this.syncMetadataService = syncMetadataService;
    }

    @GetMapping
    public SyncStatusResponse getSyncStatus(@RequestParam(defaultValue = "1") Long userId) {
        return syncMetadataService.getSyncStatus(userId);
    }
}