package com.holeyko.vkscoreboard.service;

import com.holeyko.vkscoreboard.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category save(Category category);

    Optional<Category> findById(long id);

    Optional<Category> findByName(String name);

    List<Category> findAll();
}
