package com.inboxreader.backend.controller;

import com.inboxreader.backend.dto.response.BookmarkResponse;
import com.inboxreader.backend.service.BookmarkService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/toggle")
    public BookmarkResponse toggle(@RequestParam Long userId, @RequestParam Long articleId) {
        return bookmarkService.toggleBookmark(userId, articleId);
    }

    @GetMapping
    public List<Long> getBookmarkedArticleIds(@RequestParam(defaultValue = "1") Long userId) {
        return bookmarkService.getBookmarkedArticleIds(userId);
    }
}