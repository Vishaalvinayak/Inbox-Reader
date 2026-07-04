package com.inboxreader.backend.dto.response;

import java.time.Instant;

public record ArticleResponse(
    Long id,
    String senderName,
    String subject,
    String plainText,
    Integer readingTimeMins,
    String categoryName,
    Instant receivedAt
) {}