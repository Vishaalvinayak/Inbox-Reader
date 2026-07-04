package com.inboxreader.backend.repository.impl;

import com.inboxreader.backend.entity.Bookmark;
import com.inboxreader.backend.repository.BookmarkRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryBookmarkRepository implements BookmarkRepository {

    private final Map<Long, Bookmark> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @Override
    public List<Bookmark> findAllByUserId(Long userId) {
        return store.values().stream()
                .filter(b -> b.getUserId().equals(userId))
                .toList();
    }

    @Override
    public Optional<Bookmark> findByUserIdAndArticleId(Long userId, Long articleId) {
        return store.values().stream()
                .filter(b -> b.getUserId().equals(userId) && b.getArticleId().equals(articleId))
                .findFirst();
    }

    @Override
    public Bookmark save(Bookmark bookmark) {
        if (bookmark.getId() == null) {
            bookmark.setId(idSequence.getAndIncrement());
            bookmark.setCreatedAt(Instant.now());
        }
        store.put(bookmark.getId(), bookmark);
        return bookmark;
    }

    @Override
    public void deleteByUserIdAndArticleId(Long userId, Long articleId) {
        findByUserIdAndArticleId(userId, articleId)
                .ifPresent(b -> store.remove(b.getId()));
    }
}