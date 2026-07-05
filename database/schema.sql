-- ============================================
-- Inbox Reader — Database Schema
-- Target: PostgreSQL (Neon)
-- ============================================

-- USERS
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    display_name VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ARTICLES
CREATE TABLE articles (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    sender_name VARCHAR(255),
    sender_email VARCHAR(255),
    snippet TEXT,
    content TEXT,
    gmail_message_id VARCHAR(255) UNIQUE,
    gmail_label VARCHAR(255),
    received_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- BOOKMARKS
CREATE TABLE bookmarks (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    article_id BIGINT NOT NULL REFERENCES articles(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_user_article_bookmark UNIQUE (user_id, article_id)
);

-- READING HISTORY
CREATE TABLE reading_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    article_id BIGINT NOT NULL REFERENCES articles(id) ON DELETE CASCADE,
    read_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_user_article_read UNIQUE (user_id, article_id)
);

-- SETTINGS
CREATE TABLE settings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    theme VARCHAR(50) NOT NULL DEFAULT 'light',
    gmail_label_name VARCHAR(255) NOT NULL DEFAULT 'Newsletters',
    sync_frequency_minutes INT NOT NULL DEFAULT 60,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- SYNC METADATA
CREATE TABLE sync_metadata (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    last_synced_at TIMESTAMPTZ,
    gmail_history_id VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'idle',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);