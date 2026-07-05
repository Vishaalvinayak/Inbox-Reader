package com.inboxreader.backend.dto.response;

public record SettingsResponse(
    String theme,
    String gmailLabelName,
    Integer syncFrequencyMinutes
) {}