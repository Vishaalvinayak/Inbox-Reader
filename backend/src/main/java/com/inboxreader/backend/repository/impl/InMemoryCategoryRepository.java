package com.inboxreader.backend.repository.impl;

import com.inboxreader.backend.entity.Category;
import com.inboxreader.backend.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {

    private final Map<Long, Category> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public InMemoryCategoryRepository() {
        save(new Category(null, "AI", "ai"));
        save(new Category(null, "Business", "business"));
        save(new Category(null, "Finance", "finance"));
        save(new Category(null, "Technology", "technology"));
        save(new Category(null, "World News", "world-news"));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId(idSequence.getAndIncrement());
        }
        store.put(category.getId(), category);
        return category;
    }
}