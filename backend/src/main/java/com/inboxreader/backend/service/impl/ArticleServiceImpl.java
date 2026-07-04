package com.inboxreader.backend.service.impl;

import com.inboxreader.backend.dto.response.ArticleResponse;
import com.inboxreader.backend.entity.Article;
import com.inboxreader.backend.entity.Category;
import com.inboxreader.backend.exception.ResourceNotFoundException;
import com.inboxreader.backend.mapper.ArticleMapper;
import com.inboxreader.backend.repository.ArticleRepository;
import com.inboxreader.backend.repository.CategoryRepository;
import com.inboxreader.backend.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                               CategoryRepository categoryRepository,
                               ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.articleMapper = articleMapper;
    }

    @Override
    public List<ArticleResponse> getArticlesForUser(Long userId) {
        return articleRepository.findAllByUserId(userId).stream()
                .map(this::toResponseWithCategory)
                .toList();
    }

    @Override
    public ArticleResponse getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found: " + id));
        return toResponseWithCategory(article);
    }

    private ArticleResponse toResponseWithCategory(Article article) {
        Category category = article.getCategoryId() != null
                ? categoryRepository.findById(article.getCategoryId()).orElse(null)
                : null;
        return articleMapper.toResponse(article, category);
    }
}