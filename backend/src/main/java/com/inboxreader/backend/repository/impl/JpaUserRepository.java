package com.inboxreader.backend.repository.impl;

import com.inboxreader.backend.entity.User;
import com.inboxreader.backend.repository.UserRepository;
import com.inboxreader.backend.repository.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public JpaUserRepository(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id);
    }
}