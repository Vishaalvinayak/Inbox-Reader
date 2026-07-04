package com.inboxreader.backend.repository.impl;

import com.inboxreader.backend.entity.Article;
import com.inboxreader.backend.repository.ArticleRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryArticleRepository implements ArticleRepository {

    private final Map<Long, Article> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public InMemoryArticleRepository() {
        seed();
    }

    @Override
    public List<Article> findAllByUserId(Long userId) {
        return store.values().stream()
                .filter(a -> a.getUserId().equals(userId))
                .sorted(Comparator.comparing(Article::getReceivedAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Article save(Article article) {
        if (article.getId() == null) {
            article.setId(idSequence.getAndIncrement());
            article.setCreatedAt(Instant.now());
        }
        store.put(article.getId(), article);
        return article;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    private void seed() {
        save(new Article(null, 1L, 1L, "newsletter@stratechery.com", "Stratechery",
                "Apple's Services Pivot", null,
                "Apple's shift toward services revenue continues to reshape strategy...",
                6, Instant.now().minusSeconds(3600), null));
        save(new Article(null, 1L, 2L, "digest@therundown.ai", "The Rundown AI",
                "OpenAI Ships New Reasoning Model", null,
                "A new frontier model claims state-of-the-art results on reasoning benchmarks...",
                4, Instant.now().minusSeconds(7200), null));
        save(new Article(null, 1L, 3L, "brief@morningbrew.com", "Morning Brew",
                "Markets Rally on Rate Cut Signals", null,
                "Equity markets responded positively to comments suggesting a policy shift...",
                5, Instant.now().minusSeconds(10800), null));
    }
}