package com.inboxreader.backend.repository.impl;

import com.inboxreader.backend.entity.ReadingHistory;
import com.inboxreader.backend.repository.ReadingHistoryRepository;
import com.inboxreader.backend.repository.jpa.ReadingHistoryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaReadingHistoryRepository implements ReadingHistoryRepository {

    private final ReadingHistoryJpaRepository jpaRepository;

    public JpaReadingHistoryRepository(ReadingHistoryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<ReadingHistory> findAllByUserId(Long userId) {
        return jpaRepository.findByUser_Id(userId);
    }

    @Override
    public Optional<ReadingHistory> findByUserIdAndArticleId(Long userId, Long articleId) {
        return jpaRepository.findByUser_IdAndArticle_Id(userId, articleId);
    }

    @Override
    public ReadingHistory save(ReadingHistory readingHistory) {
        return jpaRepository.save(readingHistory);
    }
}