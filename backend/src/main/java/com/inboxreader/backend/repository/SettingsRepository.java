package com.inboxreader.backend.repository;

import com.inboxreader.backend.entity.Settings;
import java.util.Optional;

public interface SettingsRepository {
    Optional<Settings> findByUserId(Long userId);
    Settings save(Settings settings);
}