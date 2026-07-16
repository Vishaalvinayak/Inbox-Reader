package com.inboxreader.backend.repository.impl;

import com.inboxreader.backend.entity.GmailCredential;
import com.inboxreader.backend.repository.GmailCredentialRepository;
import com.inboxreader.backend.repository.jpa.GmailCredentialJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaGmailCredentialRepository implements GmailCredentialRepository {

    private final GmailCredentialJpaRepository jpaRepository;

    @Override
    public GmailCredential save(GmailCredential credential) {
        return jpaRepository.save(credential);
    }

    @Override
    public Optional<GmailCredential> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        jpaRepository.deleteByUserId(userId);
    }
}