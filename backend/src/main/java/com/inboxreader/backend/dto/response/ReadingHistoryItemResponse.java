package com.inboxreader.backend.dto.response;

import java.time.OffsetDateTime;

public record ReadingHistoryItemResponse(
    Long articleId,
    String title,
    String senderName,
    String snippet,
    String gmailLabel,
    OffsetDateTime receivedAt,
    OffsetDateTime readAt
) {}