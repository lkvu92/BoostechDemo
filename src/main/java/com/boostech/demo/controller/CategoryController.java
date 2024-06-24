package com.boostech.demo.controller;

import com.boostech.demo.dto.reqDto.AddAttributeToCategoryDto;
import com.boostech.demo.dto.reqDto.category.CategoryRequestDTO;
import com.boostech.demo.dto.reqDto.category.RemoveAttributeFromCategoryDto;
import com.boostech.demo.dto.resDto.category.GetAllCategoryResponseDto;
import com.boostech.demo.entity.Category;
import com.boostech.demo.service.CategoryService;
import com.boostech.demo.util.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;
    private static final String SUCCESS_MESSAGE = "success";

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<CustomResponse<List<GetAllCategoryResponseDto>>> getAllCategories() {
        List<GetAllCategoryResponseDto> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            CustomResponse<List<GetAllCategoryResponseDto>> response = new CustomResponse<>
                    ("No categories found", HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        CustomResponse<List<GetAllCategoryResponseDto>> response = new CustomResponse<>
                (SUCCESS_MESSAGE, HttpStatus.OK.value(), categories);
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
                    (SUCCESS_MESSAGE, HttpStatus.OK.value(), categoryOpt.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        CustomResponse<Category> response = new CustomResponse<>
                ("Category not found", HttpStatus.NOT_FOUND.value(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getOneWithoutAttributes/{id}")
    public ResponseEntity<CustomResponse<GetAllCategoryResponseDto>> getOneCategoryWithoutAttributes(@PathVariable UUID id) {
        Optional<GetAllCategoryResponseDto> categoryOpt = categoryService.getOneCategoryWithoutAttributes(id);
        if (categoryOpt.isPresent()) {
            CustomResponse<GetAllCategoryResponseDto> response = new CustomResponse<>
                    (SUCCESS_MESSAGE, HttpStatus.OK.value(), categoryOpt.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        CustomResponse<GetAllCategoryResponseDto> response = new CustomResponse<>
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
        CustomResponse<Category> response = new CustomResponse<>(SUCCESS_MESSAGE, HttpStatus.CREATED.value(), category);
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
                (SUCCESS_MESSAGE, HttpStatus.OK.value(), category);
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


    @PostMapping("/addAttributes/{categoryId}")
    public  ResponseEntity<CustomResponse<String>> addAttributeToCategory (@PathVariable UUID categoryId,@RequestBody AddAttributeToCategoryDto dto){
        try {
            categoryService.addAttributeToCategory(categoryId, dto);
            CustomResponse<String> response = new CustomResponse<>
                    ("Attribute added successfully", HttpStatus.OK.value(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (RuntimeException e){
            CustomResponse<String> response = new CustomResponse<>
                    (e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/removeAttributes/{categoryId}")
    public ResponseEntity<CustomResponse<String>> removeAttributesFromCategory(@PathVariable UUID categoryId, @RequestBody RemoveAttributeFromCategoryDto dto) {
        try {
            categoryService.removeAttributesFromCategory(categoryId, dto);
            CustomResponse<String> response = new CustomResponse<>("Attributes removed successfully", HttpStatus.OK.value(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            CustomResponse<String> response = new CustomResponse<>(e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
