package com.inboxreader.backend.service;

import com.inboxreader.backend.dto.response.ArticleResponse;
import java.util.List;

public interface ArticleService {
    List<ArticleResponse> getArticlesForUser(Long userId);
    ArticleResponse getArticleById(Long id);
}