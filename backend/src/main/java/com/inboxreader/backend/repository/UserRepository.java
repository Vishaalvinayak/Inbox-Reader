package com.inboxreader.backend.repository;

import com.inboxreader.backend.entity.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
}