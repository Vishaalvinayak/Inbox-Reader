package com.inboxreader.backend.repository.impl;

import com.inboxreader.backend.entity.Article;
import com.inboxreader.backend.repository.ArticleRepository;
import com.inboxreader.backend.repository.jpa.ArticleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaArticleRepository implements ArticleRepository {

    private final ArticleJpaRepository jpaRepository;

    public JpaArticleRepository(ArticleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Article> findAllByUserId(Long userId) {
        return jpaRepository.findByUser_IdOrderByReceivedAtDesc(userId);
    }

    @Override
    public Optional<Article> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Article save(Article article) {
        return jpaRepository.save(article);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}