package com.inboxreader.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(name = "sender_name", length = 255)
    private String senderName;

    @Column(name = "sender_email", length = 255)
    private String senderEmail;

    @Column(columnDefinition = "TEXT")
    private String snippet;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "gmail_message_id", unique = true, length = 255)
    private String gmailMessageId;

    @Column(name = "gmail_label", length = 255)
    private String gmailLabel;

    @Column(name = "received_at")
    private OffsetDateTime receivedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}