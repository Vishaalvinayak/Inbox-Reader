package com.inboxreader.backend.repository.jpa;

import com.inboxreader.backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleJpaRepository extends JpaRepository<Article, Long> {
    List<Article> findByUser_IdOrderByReceivedAtDesc(Long userId);


    boolean existsByGmailMessageId(String gmailMessageId);
}