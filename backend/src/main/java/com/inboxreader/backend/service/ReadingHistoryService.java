package com.inboxreader.backend.service;

import com.inboxreader.backend.dto.response.ReadingHistoryItemResponse;
import com.inboxreader.backend.dto.response.ReadingHistoryResponse;

import java.util.List;

public interface ReadingHistoryService {
    ReadingHistoryResponse markAsRead(Long userId, Long articleId);
    List<Long> getReadArticleIds(Long userId);
    List<ReadingHistoryItemResponse> getReadHistoryDetails(Long userId);
}