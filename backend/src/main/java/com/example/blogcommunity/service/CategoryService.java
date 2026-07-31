package com.example.blogcommunity.service;

import com.example.blogcommunity.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long categoryId);

    CategoryResponse createCategory(String name, String description);

    CategoryResponse updateCategory(Long categoryId, String name, String description);

    void deleteCategory(Long categoryId);
}