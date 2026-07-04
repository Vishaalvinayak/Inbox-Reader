package com.inboxreader.backend.repository;

import com.inboxreader.backend.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Category save(Category category);
}