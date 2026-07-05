
package com.inboxreader.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "settings")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 50)
    private String theme;

    @Column(name = "gmail_label_name", nullable = false, length = 255)
    private String gmailLabelName;

    @Column(name = "sync_frequency_minutes", nullable = false)
    private Integer syncFrequencyMinutes;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}