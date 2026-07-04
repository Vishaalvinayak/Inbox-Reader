package com.inboxreader.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String senderEmail;
    private String senderName;
    private String subject;
    private String cleanedHtml;
    private String plainText;
    private Integer readingTimeMins;
    private Instant receivedAt;
    private Instant createdAt;
}