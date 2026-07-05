package com.inboxreader.backend.dto.response;

public record ReadingHistoryResponse(
    Long articleId,
    boolean read
) {}