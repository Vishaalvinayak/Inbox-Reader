package com.inboxreader.backend.repository;

import com.inboxreader.backend.entity.GmailCredential;

import java.util.Optional;

public interface GmailCredentialRepository {
    GmailCredential save(GmailCredential credential);
    Optional<GmailCredential> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}