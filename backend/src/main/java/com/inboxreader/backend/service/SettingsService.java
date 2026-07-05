package com.inboxreader.backend.service;

import com.inboxreader.backend.dto.request.UpdateSettingsRequest;
import com.inboxreader.backend.dto.response.SettingsResponse;

public interface SettingsService {
    SettingsResponse getSettings(Long userId);
    SettingsResponse updateSettings(Long userId, UpdateSettingsRequest request);
}