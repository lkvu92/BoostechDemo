package com.boostech.demo.controller;

import com.boostech.demo.dto.CategoryDTO;
import com.boostech.demo.entity.Category;
import com.boostech.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * Get category by id
     * @param id category id
     * @return category
     */
    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDTO categoryDTO) {

        Category category = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @RequestBody CategoryDTO categoryDTO) {

        Category category = categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/addAttributes")
    public Category addAttributesToCategory(@PathVariable UUID id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.addAttributesToCategory(id, categoryDTO.getAttributeIds());
    }

    @PostMapping("/{id}/removeAttributes")
    public Category removeAttributesFromCategory(@PathVariable UUID id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.removeAttributesFromCategory(id, categoryDTO.getAttributeIds());
    }
}
