package org.wcs.myblog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wcs.myblog.dto.ArticleDTO;
import org.wcs.myblog.dto.CategoryDTO;
import org.wcs.myblog.model.Article;
import org.wcs.myblog.model.Category;
import org.wcs.myblog.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")

public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<CategoryDTO> categoryDTOs = categories.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
    Category category = categoryRepository.findById(id).orElse(null);
    if (category == null) {
        return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(category));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(convertToDTO(savedCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {

        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
        return ResponseEntity.notFound().build();
        }
        category.setName(categoryDetails.getName());

        Category updateCategory = categoryRepository.save(category);
        return ResponseEntity.ok(convertToDTO(updateCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        if (category.getArticles() != null) {
            categoryDTO.setArticles(category.getArticles().stream().map(article -> {
                ArticleDTO articleDTO = new ArticleDTO();
                articleDTO.setId(article.getId());
                articleDTO.setTitle(article.getTitle());
                articleDTO.setContent(article.getContent());
                articleDTO.setUpdatedAt(article.getUpdatedAt());
                articleDTO.setCategoryName(article.getCategory().getName());
                return articleDTO;
            }).collect(Collectors.toList()));
        }
        return categoryDTO;
    }
}

