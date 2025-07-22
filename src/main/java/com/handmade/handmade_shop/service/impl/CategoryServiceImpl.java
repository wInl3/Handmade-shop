package com.handmade.handmade_shop.service.impl;

import com.handmade.handmade_shop.entity.Category;
import com.handmade.handmade_shop.repository.CategoryRepository;
import com.handmade.handmade_shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(String name) {
        if (categoryRepository.findByName(name).isPresent())
            throw new RuntimeException("Category already exists");
        return categoryRepository.save(new Category(null, name, null));
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }
}

