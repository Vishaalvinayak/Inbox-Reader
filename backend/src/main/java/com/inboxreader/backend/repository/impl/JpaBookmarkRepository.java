package com.inboxreader.backend.repository.impl;

import com.inboxreader.backend.entity.Bookmark;
import com.inboxreader.backend.repository.BookmarkRepository;
import com.inboxreader.backend.repository.jpa.BookmarkJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaBookmarkRepository implements BookmarkRepository {

    private final BookmarkJpaRepository jpaRepository;

    public JpaBookmarkRepository(BookmarkJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Bookmark> findAllByUserId(Long userId) {
        return jpaRepository.findByUser_Id(userId);
    }

    @Override
    public Optional<Bookmark> findByUserIdAndArticleId(Long userId, Long articleId) {
        return jpaRepository.findByUser_IdAndArticle_Id(userId, articleId);
    }

    @Override
    public Bookmark save(Bookmark bookmark) {
        return jpaRepository.save(bookmark);
    }

    @Override
    public void deleteByUserIdAndArticleId(Long userId, Long articleId) {
        jpaRepository.deleteByUser_IdAndArticle_Id(userId, articleId);
    }
}