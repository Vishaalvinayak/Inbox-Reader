package com.inboxreader.backend.repository.jpa;

import com.inboxreader.backend.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingsJpaRepository extends JpaRepository<Settings, Long> {
    Optional<Settings> findByUser_Id(Long userId);
}