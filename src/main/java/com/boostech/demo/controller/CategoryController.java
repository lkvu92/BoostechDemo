package com.boostech.demo.controller;

import com.boostech.demo.dto.CategoryDTO;
import com.boostech.demo.entity.Category;
import com.boostech.demo.service.CategoryService;
import com.boostech.demo.util.CustomResponse;
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
    public ResponseEntity<CustomResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        CustomResponse<List<Category>> response = new CustomResponse<>("Success", HttpStatus.OK.value(), categories);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get category by id
     *
     * @param id category id
     * @return category
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Category>> getCategoryById(@PathVariable UUID id) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        if (categoryOpt.isPresent()) {
            CustomResponse<Category> response = new CustomResponse<>
                    ("Success", HttpStatus.OK.value(), categoryOpt.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        CustomResponse<Category> response = new CustomResponse<>
                ("Category not found", HttpStatus.NOT_FOUND.value(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Create a new category
     *
     * @param categoryDTO category data
     * @return category
     */
    @PostMapping("/create")
    public ResponseEntity<CustomResponse<Category>> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.createCategory(categoryDTO);
        CustomResponse<Category> response = new CustomResponse<>("Success", HttpStatus.CREATED.value(), category);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update a category
     *
     * @param id category id
     * @param categoryDTO category data
     * @return category
     */

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Category>> updateCategory(
            @PathVariable UUID id,
            @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.updateCategory(id, categoryDTO);
        CustomResponse<Category> response = new CustomResponse<>
                ("Success", HttpStatus.OK.value(), category);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Delete a category
     *
     * @param id category id
     * @return success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<String>> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        CustomResponse<String> response = new CustomResponse<>
                ("Category deleted successfully", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Add attributes to category
     *
     * @param id category id
     * @param categoryDTO category data
     * @return category
     */
    @PostMapping("/{id}/addAttributes")
    public ResponseEntity<CustomResponse<Category>> addAttributesToCategory(@PathVariable UUID id, @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.addAttributesToCategory(id, categoryDTO.getAttributeIds());
        CustomResponse<Category> response = new CustomResponse<>("Success", HttpStatus.OK.value(), category);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Remove attributes from category
     *
     * @param id category id
     * @param categoryDTO category data
     * @return category
     */
    @PostMapping("/{id}/removeAttributes")
    public ResponseEntity<CustomResponse<Category>> removeAttributesFromCategory(
            @PathVariable UUID id,
            @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.removeAttributesFromCategory(id, categoryDTO.getAttributeIds());
        CustomResponse<Category> response = new CustomResponse<>
                ("Success", HttpStatus.OK.value(), category);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
