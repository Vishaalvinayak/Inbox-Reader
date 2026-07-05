package com.inboxreader.backend.controller;

import com.inboxreader.backend.dto.request.UpdateSettingsRequest;
import com.inboxreader.backend.dto.response.SettingsResponse;
import com.inboxreader.backend.service.SettingsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public SettingsResponse getSettings(@RequestParam(defaultValue = "1") Long userId) {
        return settingsService.getSettings(userId);
    }

    @PutMapping
    public SettingsResponse updateSettings(@RequestParam(defaultValue = "1") Long userId,
                                            @RequestBody UpdateSettingsRequest request) {
        return settingsService.updateSettings(userId, request);
    }
}