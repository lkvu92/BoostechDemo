package com.boostech.demo.controller;

import com.boostech.demo.dto.CategoryRequestDTO;
import com.boostech.demo.dto.CategoryResponseDto;
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
    public ResponseEntity<CustomResponse<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            CustomResponse<List<CategoryResponseDto>> response = new CustomResponse<>
                    ("No categories found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        CustomResponse<List<CategoryResponseDto>> response = new CustomResponse<>
                ("Success", HttpStatus.OK.value(), categories);
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
     * @param categoryRequestDTO category data
     * @return category
     */
    @PostMapping("/create")
    public ResponseEntity<CustomResponse<Category>> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryService.createCategory(categoryRequestDTO);
        CustomResponse<Category> response = new CustomResponse<>("Success", HttpStatus.CREATED.value(), category);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update a category
     *
     * @param id category id
     * @param categoryRequestDTO category data
     * @return category
     */

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Category>> updateCategory(
            @PathVariable UUID id,
            @RequestBody CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryService.updateCategory(id, categoryRequestDTO);
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
        try {
            categoryService.deleteCategory(id);
            CustomResponse<String> response = new CustomResponse<>
                    ("Category deleted successfully", HttpStatus.OK.value(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            CustomResponse<String> response = new CustomResponse<>
                    (e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Add attributes to category
     *
     * @param id category id
     * @param categoryRequestDTO category data
     * @return category
     */
    @PostMapping("/addAttributes/{id}")
    public ResponseEntity<CustomResponse<Category>> addAttributesToCategory(@PathVariable UUID id, @RequestBody CategoryRequestDTO categoryRequestDTO) {
        try {
            Category category = categoryService.addAttributesToCategory(id, categoryRequestDTO.getAttributeIds());
            CustomResponse<Category> response = new CustomResponse<>("Success", HttpStatus.OK.value(), category);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            CustomResponse<Category> response = new CustomResponse<>
                    (e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Remove attributes from category
     *
     * @param id category id
     * @param categoryRequestDTO category data
     * @return category
     */
    @PostMapping("/removeAttributes/{id}")
    public ResponseEntity<CustomResponse<Category>> removeAttributesFromCategory(
            @PathVariable UUID id,
            @RequestBody CategoryRequestDTO categoryRequestDTO) {
        try {
            Category category = categoryService.removeAttributesFromCategory(id, categoryRequestDTO.getAttributeIds());
            CustomResponse<Category> response = new CustomResponse<>
                    ("Success", HttpStatus.OK.value(), category);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            CustomResponse<Category> response = new CustomResponse<>
                    (e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
