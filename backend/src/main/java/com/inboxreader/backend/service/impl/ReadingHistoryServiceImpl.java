package com.inboxreader.backend.service.impl;

import com.inboxreader.backend.dto.response.ReadingHistoryResponse;
import com.inboxreader.backend.entity.Article;
import com.inboxreader.backend.entity.ReadingHistory;
import com.inboxreader.backend.entity.User;
import com.inboxreader.backend.exception.ResourceNotFoundException;
import com.inboxreader.backend.repository.ArticleRepository;
import com.inboxreader.backend.repository.ReadingHistoryRepository;
import com.inboxreader.backend.repository.UserRepository;
import com.inboxreader.backend.service.ReadingHistoryService;
import org.springframework.stereotype.Service;
import com.inboxreader.backend.dto.response.ReadingHistoryItemResponse;
import java.util.Comparator;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ReadingHistoryServiceImpl implements ReadingHistoryService {

    private final ReadingHistoryRepository readingHistoryRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ReadingHistoryServiceImpl(ReadingHistoryRepository readingHistoryRepository,
                                      ArticleRepository articleRepository,
                                      UserRepository userRepository) {
        this.readingHistoryRepository = readingHistoryRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ReadingHistoryResponse markAsRead(Long userId, Long articleId) {
        var existing = readingHistoryRepository.findByUserIdAndArticleId(userId, articleId);
        if (existing.isPresent()) {
            return new ReadingHistoryResponse(articleId, true);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found: " + articleId));

        ReadingHistory readingHistory = new ReadingHistory(null, user, article, OffsetDateTime.now());
        readingHistoryRepository.save(readingHistory);
        return new ReadingHistoryResponse(articleId, true);
    }

    @Override
    public List<Long> getReadArticleIds(Long userId) {
        return readingHistoryRepository.findAllByUserId(userId).stream()
                .map(rh -> rh.getArticle().getId())
                .toList();
    }
    @Override
    public List<ReadingHistoryItemResponse> getReadHistoryDetails(Long userId) {
        return readingHistoryRepository.findAllByUserId(userId).stream()
                .sorted(Comparator.comparing(ReadingHistory::getReadAt).reversed())
                .map(rh -> new ReadingHistoryItemResponse(
                        rh.getArticle().getId(),
                        rh.getArticle().getTitle(),
                        rh.getArticle().getSenderName(),
                        rh.getArticle().getSnippet(),
                        rh.getArticle().getGmailLabel(),
                        rh.getArticle().getReceivedAt(),
                        rh.getReadAt()
                ))
                .toList();
    }
}