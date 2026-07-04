package com.inboxreader.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {
    private Long id;
    private Long userId;
    private Long articleId;
    private Instant createdAt;
}