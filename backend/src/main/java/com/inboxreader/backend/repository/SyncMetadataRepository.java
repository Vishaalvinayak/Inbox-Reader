package com.inboxreader.backend.repository;

import com.inboxreader.backend.entity.SyncMetadata;
import java.util.Optional;

public interface SyncMetadataRepository {
    Optional<SyncMetadata> findLatestByUserId(Long userId);
    SyncMetadata save(SyncMetadata syncMetadata);
}