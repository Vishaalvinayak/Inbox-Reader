package com.inboxreader.backend.service.impl;

import com.inboxreader.backend.dto.request.UpdateSettingsRequest;
import com.inboxreader.backend.dto.response.SettingsResponse;
import com.inboxreader.backend.entity.Settings;
import com.inboxreader.backend.entity.User;
import com.inboxreader.backend.exception.ResourceNotFoundException;
import com.inboxreader.backend.repository.SettingsRepository;
import com.inboxreader.backend.repository.UserRepository;
import com.inboxreader.backend.service.SettingsService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class SettingsServiceImpl implements SettingsService {

    private static final String DEFAULT_THEME = "light";
    private static final String DEFAULT_LABEL = "Newsletters";
    private static final int DEFAULT_SYNC_FREQUENCY = 60;

    private final SettingsRepository settingsRepository;
    private final UserRepository userRepository;

    public SettingsServiceImpl(SettingsRepository settingsRepository, UserRepository userRepository) {
        this.settingsRepository = settingsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SettingsResponse getSettings(Long userId) {
        Settings settings = settingsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSettings(userId));
        return toResponse(settings);
    }

    @Override
    public SettingsResponse updateSettings(Long userId, UpdateSettingsRequest request) {
        Settings settings = settingsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSettings(userId));

        if (request.theme() != null) settings.setTheme(request.theme());
        if (request.gmailLabelName() != null) settings.setGmailLabelName(request.gmailLabelName());
        if (request.syncFrequencyMinutes() != null) settings.setSyncFrequencyMinutes(request.syncFrequencyMinutes());
        settings.setUpdatedAt(OffsetDateTime.now());

        return toResponse(settingsRepository.save(settings));
    }

    private Settings createDefaultSettings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        Settings settings = new Settings(null, user, DEFAULT_THEME, DEFAULT_LABEL,
                DEFAULT_SYNC_FREQUENCY, OffsetDateTime.now());
        return settingsRepository.save(settings);
    }

    private SettingsResponse toResponse(Settings settings) {
        return new SettingsResponse(
                settings.getTheme(),
                settings.getGmailLabelName(),
                settings.getSyncFrequencyMinutes()
        );
    }
}