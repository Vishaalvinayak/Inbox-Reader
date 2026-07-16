package com.inboxreader.backend.repository.jpa;

import com.inboxreader.backend.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkJpaRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUser_Id(Long userId);
    Optional<Bookmark> findByUser_IdAndArticle_Id(Long userId, Long articleId);
    void deleteByUser_IdAndArticle_Id(Long userId, Long articleId);
}