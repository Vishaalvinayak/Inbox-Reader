package com.inboxreader.backend.repository.impl;

import com.inboxreader.backend.entity.Settings;
import com.inboxreader.backend.repository.SettingsRepository;
import com.inboxreader.backend.repository.jpa.SettingsJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaSettingsRepository implements SettingsRepository {

    private final SettingsJpaRepository jpaRepository;

    public JpaSettingsRepository(SettingsJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Settings> findByUserId(Long userId) {
        return jpaRepository.findByUser_Id(userId);
    }

    @Override
    public Settings save(Settings settings) {
        return jpaRepository.save(settings);
    }
}