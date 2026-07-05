package com.inboxreader.backend.dto.response;

import java.time.OffsetDateTime;

public record ArticleResponse(
    Long id,
    String senderName,
    String title,
    String snippet,
    Integer readingTimeMins,
    String gmailLabel,
    OffsetDateTime receivedAt
) {}