package com.inboxreader.backend.controller;

import com.inboxreader.backend.dto.response.ReadingHistoryResponse;
import com.inboxreader.backend.service.ReadingHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reading-history")
public class ReadingHistoryController {

    private final ReadingHistoryService readingHistoryService;

    public ReadingHistoryController(ReadingHistoryService readingHistoryService) {
        this.readingHistoryService = readingHistoryService;
    }

    @PostMapping
    public ReadingHistoryResponse markAsRead(@RequestParam Long userId, @RequestParam Long articleId) {
        return readingHistoryService.markAsRead(userId, articleId);
    }

    @GetMapping
    public List<Long> getReadArticleIds(@RequestParam(defaultValue = "1") Long userId) {
        return readingHistoryService.getReadArticleIds(userId);
    }
}