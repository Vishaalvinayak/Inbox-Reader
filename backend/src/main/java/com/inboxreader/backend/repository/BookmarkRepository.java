package com.inboxreader.backend.repository;

import com.inboxreader.backend.entity.Bookmark;
import java.util.List;
import java.util.Optional;

public interface BookmarkRepository {
    List<Bookmark> findAllByUserId(Long userId);
    Optional<Bookmark> findByUserIdAndArticleId(Long userId, Long articleId);
    Bookmark save(Bookmark bookmark);
    void deleteByUserIdAndArticleId(Long userId, Long articleId);
}