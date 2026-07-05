package com.inboxreader.backend.repository.jpa;

import com.inboxreader.backend.entity.ReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReadingHistoryJpaRepository extends JpaRepository<ReadingHistory, Long> {
    List<ReadingHistory> findByUser_Id(Long userId);
    Optional<ReadingHistory> findByUser_IdAndArticle_Id(Long userId, Long articleId);
}