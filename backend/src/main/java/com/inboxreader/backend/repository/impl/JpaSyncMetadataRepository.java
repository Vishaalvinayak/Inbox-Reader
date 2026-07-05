package com.inboxreader.backend.repository.impl;

import com.inboxreader.backend.entity.SyncMetadata;
import com.inboxreader.backend.repository.SyncMetadataRepository;
import com.inboxreader.backend.repository.jpa.SyncMetadataJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaSyncMetadataRepository implements SyncMetadataRepository {

    private final SyncMetadataJpaRepository jpaRepository;

    public JpaSyncMetadataRepository(SyncMetadataJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<SyncMetadata> findLatestByUserId(Long userId) {
        return jpaRepository.findFirstByUser_IdOrderByCreatedAtDesc(userId);
    }

    @Override
    public SyncMetadata save(SyncMetadata syncMetadata) {
        return jpaRepository.save(syncMetadata);
    }
}