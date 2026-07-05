package com.inboxreader.backend.mapper;

import com.inboxreader.backend.dto.response.ArticleResponse;
import com.inboxreader.backend.entity.Article;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    private static final int WORDS_PER_MINUTE = 200;

    public ArticleResponse toResponse(Article article) {
        int wordCount = article.getContent() != null
                ? article.getContent().trim().split("\\s+").length
                : 0;
        int readingTimeMins = Math.max(1, wordCount / WORDS_PER_MINUTE);

        return new ArticleResponse(
                article.getId(),
                article.getSenderName(),
                article.getTitle(),
                article.getSnippet(),
                readingTimeMins,
                article.getGmailLabel(),
                article.getReceivedAt()
        );
    }
}