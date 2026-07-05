package com.inboxreader.backend.service.impl;

import com.inboxreader.backend.dto.response.BookmarkResponse;
import com.inboxreader.backend.entity.Article;
import com.inboxreader.backend.entity.Bookmark;
import com.inboxreader.backend.entity.User;
import com.inboxreader.backend.exception.ResourceNotFoundException;
import com.inboxreader.backend.repository.ArticleRepository;
import com.inboxreader.backend.repository.BookmarkRepository;
import com.inboxreader.backend.repository.UserRepository;
import com.inboxreader.backend.service.BookmarkService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository,
                                ArticleRepository articleRepository,
                                UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BookmarkResponse toggleBookmark(Long userId, Long articleId) {
        var existing = bookmarkRepository.findByUserIdAndArticleId(userId, articleId);

        if (existing.isPresent()) {
            bookmarkRepository.deleteByUserIdAndArticleId(userId, articleId);
            return new BookmarkResponse(articleId, false);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found: " + articleId));

        Bookmark bookmark = new Bookmark(null, user, article, OffsetDateTime.now());
        bookmarkRepository.save(bookmark);
        return new BookmarkResponse(articleId, true);
    }

    @Override
    public List<Long> getBookmarkedArticleIds(Long userId) {
        return bookmarkRepository.findAllByUserId(userId).stream()
                .map(b -> b.getArticle().getId())
                .toList();
    }
}