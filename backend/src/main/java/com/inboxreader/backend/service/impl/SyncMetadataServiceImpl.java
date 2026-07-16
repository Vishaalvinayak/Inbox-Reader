package com.inboxreader.backend.service.impl;

import com.inboxreader.backend.dto.response.SyncStatusResponse;
import com.inboxreader.backend.entity.SyncMetadata;
import com.inboxreader.backend.entity.User;
import com.inboxreader.backend.repository.SyncMetadataRepository;
import com.inboxreader.backend.repository.UserRepository;
import com.inboxreader.backend.service.SyncMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class SyncMetadataServiceImpl implements SyncMetadataService {

    private static final Logger log = LoggerFactory.getLogger(SyncMetadataServiceImpl.class);

    private final SyncMetadataRepository syncMetadataRepository;
    private final UserRepository userRepository;

    public SyncMetadataServiceImpl(SyncMetadataRepository syncMetadataRepository,
                                     UserRepository userRepository) {
        this.syncMetadataRepository = syncMetadataRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SyncStatusResponse getSyncStatus(Long userId) {
        return syncMetadataRepository.findLatestByUserId(userId)
                .map(sm -> new SyncStatusResponse(sm.getLastSyncedAt(), sm.getStatus()))
                .orElse(new SyncStatusResponse(null, "never_synced"));
    }

  @Override
public void recordSuccess(Long userId, int articlesImported) {
    User user = getUser(userId);
    OffsetDateTime now = OffsetDateTime.now();
    SyncMetadata syncMetadata = new SyncMetadata(null, user, now, null, "success", now);
    syncMetadataRepository.save(syncMetadata);
    log.info("Gmail sync succeeded for user {}: {} articles imported", userId, articlesImported);
}

    @Override
    public void recordFailure(Long userId, String errorMessage) {
        log.warn("Gmail sync failed for user {}: {}", userId, errorMessage);
        User user = getUser(userId);
        OffsetDateTime now = OffsetDateTime.now();
        SyncMetadata syncMetadata = new SyncMetadata(null, user, now, null, "failed", now);
        syncMetadataRepository.save(syncMetadata);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found: " + userId));
    }
}