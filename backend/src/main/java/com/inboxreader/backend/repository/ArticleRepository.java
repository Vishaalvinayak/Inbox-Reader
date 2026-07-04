package com.inboxreader.backend.repository;

import com.inboxreader.backend.entity.Article;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    List<Article> findAllByUserId(Long userId);
    Optional<Article> findById(Long id);
    Article save(Article article);
    void deleteById(Long id);
}