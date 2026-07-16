CREATE TABLE gmail_credentials (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    access_token TEXT NOT NULL,
    refresh_token TEXT NOT NULL,
    token_expiry TIMESTAMP NOT NULL,
    scope VARCHAR(255) NOT NULL,
    connected_at TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (user_id)
);