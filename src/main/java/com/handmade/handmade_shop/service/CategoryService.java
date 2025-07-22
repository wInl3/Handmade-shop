package com.handmade.handmade_shop.service;

import com.handmade.handmade_shop.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category createCategory(String name);

    List<Category> getAll();

    void delete(UUID id);
}
