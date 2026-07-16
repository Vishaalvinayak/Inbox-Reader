package com.inboxreader.backend.service;

public interface GmailSyncService {
    int syncNewsletters(Long userId);
}