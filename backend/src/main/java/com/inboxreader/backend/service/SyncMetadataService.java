package com.inboxreader.backend.service;

import com.inboxreader.backend.dto.response.SyncStatusResponse;

public interface SyncMetadataService {
    SyncStatusResponse getSyncStatus(Long userId);
    void recordSuccess(Long userId, int articlesImported);
    void recordFailure(Long userId, String errorMessage);
}