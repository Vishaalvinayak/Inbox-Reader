package com.inboxreader.backend.repository.jpa;

import com.inboxreader.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}