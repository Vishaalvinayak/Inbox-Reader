package com.inboxreader.backend.dto.response;

public record BookmarkResponse(
    Long articleId,
    boolean bookmarked
) {}