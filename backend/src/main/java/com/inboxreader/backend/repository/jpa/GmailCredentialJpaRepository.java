package com.inboxreader.backend.repository.jpa;

import com.inboxreader.backend.entity.GmailCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GmailCredentialJpaRepository extends JpaRepository<GmailCredential, Long> {
    Optional<GmailCredential> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}