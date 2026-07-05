package com.inboxreader.backend.service.impl;

import com.inboxreader.backend.dto.response.SyncStatusResponse;
import com.inboxreader.backend.repository.SyncMetadataRepository;
import com.inboxreader.backend.service.SyncMetadataService;
import org.springframework.stereotype.Service;

@Service
public class SyncMetadataServiceImpl implements SyncMetadataService {

    private final SyncMetadataRepository syncMetadataRepository;

    public SyncMetadataServiceImpl(SyncMetadataRepository syncMetadataRepository) {
        this.syncMetadataRepository = syncMetadataRepository;
    }

    @Override
    public SyncStatusResponse getSyncStatus(Long userId) {
        return syncMetadataRepository.findLatestByUserId(userId)
                .map(sm -> new SyncStatusResponse(sm.getLastSyncedAt(), sm.getStatus()))
                .orElse(new SyncStatusResponse(null, "never_synced"));
    }
}