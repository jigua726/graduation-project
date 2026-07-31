package com.example.blogcommunity.service.impl;

import com.example.blogcommunity.dto.response.CategoryResponse;
import com.example.blogcommunity.entity.Category;
import com.example.blogcommunity.exception.BusinessException;
import com.example.blogcommunity.exception.ResourceNotFoundException;
import com.example.blogcommunity.repository.ArticleRepository;
import com.example.blogcommunity.repository.CategoryRepository;
import com.example.blogcommunity.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ArticleRepository articleRepository) {
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        return toCategoryResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(String name, String description) {
        if (categoryRepository.existsByName(name)) {
            throw new BusinessException("分类名称已存在");
        }

        Category category = Category.builder()
                .name(name)
                .description(description)
                .articleCount(0)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return toCategoryResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long categoryId, String name, String description) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        if (name != null && !name.equals(category.getName())) {
            if (categoryRepository.existsByName(name)) {
                throw new BusinessException("分类名称已存在");
            }
            category.setName(name);
        }

        if (description != null) {
            category.setDescription(description);
        }

        Category savedCategory = categoryRepository.save(category);
        return toCategoryResponse(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        long actualCount = articleRepository.countByCategoryId(categoryId);
        if (actualCount > 0) {
            throw new BusinessException("该分类下存在文章，无法删除");
        }

        categoryRepository.delete(category);
    }

    private CategoryResponse toCategoryResponse(Category category) {
        long actualCount = articleRepository.countByCategoryId(category.getId());
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .articleCount((int) actualCount)
                .build();
    }
}