package vica.SubWatch.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vica.SubWatch.domain.Category;
import vica.SubWatch.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category findOrCreateCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            throw new IllegalArgumentException("Category name must not be blank");
        }

        String normalizedName = categoryName.trim();

        return categoryRepository.findByNameIgnoreCase(normalizedName)
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setName(normalizedName);
                    return categoryRepository.save(category);
                });
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
