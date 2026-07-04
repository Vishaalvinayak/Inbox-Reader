package com.inboxreader.backend.mapper;

import com.inboxreader.backend.dto.response.ArticleResponse;
import com.inboxreader.backend.entity.Article;
import com.inboxreader.backend.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public ArticleResponse toResponse(Article article, Category category) {
        return new ArticleResponse(
                article.getId(),
                article.getSenderName(),
                article.getSubject(),
                article.getPlainText(),
                article.getReadingTimeMins(),
                category != null ? category.getName() : "Uncategorized",
                article.getReceivedAt()
        );
    }
}