package com.inboxreader.backend.repository.jpa;

import com.inboxreader.backend.entity.SyncMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SyncMetadataJpaRepository extends JpaRepository<SyncMetadata, Long> {
    Optional<SyncMetadata> findFirstByUser_IdOrderByCreatedAtDesc(Long userId);
}