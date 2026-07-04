package com.inboxreader.backend.controller;

import com.inboxreader.backend.dto.response.ArticleResponse;
import com.inboxreader.backend.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleResponse> getArticles(@RequestParam(defaultValue = "1") Long userId) {
        return articleService.getArticlesForUser(userId);
    }

    @GetMapping("/{id}")
    public ArticleResponse getArticle(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }
}