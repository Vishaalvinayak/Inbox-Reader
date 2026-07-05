package com.inboxreader.backend.dto.response;

import java.time.OffsetDateTime;

public record SyncStatusResponse(
    OffsetDateTime lastSyncedAt,
    String status
) {}