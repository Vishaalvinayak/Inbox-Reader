package com.inboxreader.backend.repository;

import com.inboxreader.backend.entity.ReadingHistory;
import java.util.List;
import java.util.Optional;

public interface ReadingHistoryRepository {
    List<ReadingHistory> findAllByUserId(Long userId);
    Optional<ReadingHistory> findByUserIdAndArticleId(Long userId, Long articleId);
    ReadingHistory save(ReadingHistory readingHistory);
}