package com.inboxreader.backend.service.impl;

import com.inboxreader.backend.dto.response.BookmarkResponse;
import com.inboxreader.backend.entity.Bookmark;
import com.inboxreader.backend.repository.BookmarkRepository;
import com.inboxreader.backend.service.BookmarkService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    @Override
    public BookmarkResponse toggleBookmark(Long userId, Long articleId) {
        var existing = bookmarkRepository.findByUserIdAndArticleId(userId, articleId);

        if (existing.isPresent()) {
            bookmarkRepository.deleteByUserIdAndArticleId(userId, articleId);
            return new BookmarkResponse(articleId, false);
        } else {
            bookmarkRepository.save(new Bookmark(null, userId, articleId, null));
            return new BookmarkResponse(articleId, true);
        }
    }

    @Override
    public List<Long> getBookmarkedArticleIds(Long userId) {
        return bookmarkRepository.findAllByUserId(userId).stream()
                .map(Bookmark::getArticleId)
                .toList();
    }
}