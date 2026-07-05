package com.inboxreader.backend.dto.request;

public record UpdateSettingsRequest(
    String theme,
    String gmailLabelName,
    Integer syncFrequencyMinutes
) {}