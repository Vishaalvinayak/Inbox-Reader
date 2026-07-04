package com.inboxreader.backend.service;

import com.inboxreader.backend.dto.response.BookmarkResponse;
import java.util.List;

public interface BookmarkService {
    BookmarkResponse toggleBookmark(Long userId, Long articleId);
    List<Long> getBookmarkedArticleIds(Long userId);
}