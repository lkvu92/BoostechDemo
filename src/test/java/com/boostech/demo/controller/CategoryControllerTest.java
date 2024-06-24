package com.boostech.demo.controller;

import com.boostech.demo.dto.reqDto.category.CategoryRequestDTO;
import com.boostech.demo.dto.resDto.category.GetAllCategoryResponseDto;
import com.boostech.demo.entity.Category;
import com.boostech.demo.service.CategoryService;
import com.boostech.demo.util.CustomResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class CategoryControllerTest {

    private final CategoryService categoryService = Mockito.mock(CategoryService.class);
    private final CategoryController categoryController = new CategoryController(categoryService);

    @Test
    void testGetAllCategories() {
        GetAllCategoryResponseDto dto = new GetAllCategoryResponseDto();
        dto.setId(UUID.randomUUID());
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(dto));

        ResponseEntity<CustomResponse<List<GetAllCategoryResponseDto>>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, Objects.requireNonNull(response.getBody()).getData().get(0));
    }

    @Test
    void testCreateCategory() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Test");
        Category category = new Category();
        category.setName(requestDTO.getName());
        when(categoryService.createCategory(requestDTO)).thenReturn(category);

        ResponseEntity<CustomResponse<Category>> response = categoryController.createCategory(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(category, Objects.requireNonNull(response.getBody()).getData());
    }

    @Test
    void testUpdateCategory() {
        UUID id = UUID.randomUUID();
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Test");
        Category category = new Category();
        category.setName(requestDTO.getName());
        when(categoryService.updateCategory(id, requestDTO)).thenReturn(category);

        ResponseEntity<CustomResponse<Category>> response = categoryController.updateCategory(id, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, Objects.requireNonNull(response.getBody()).getData());
    }

    @Test
    void testDeleteCategory() {
        UUID id = UUID.randomUUID();
        doNothing().when(categoryService).deleteCategory(id);

        ResponseEntity<CustomResponse<String>> response = categoryController.deleteCategory(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddAttributesToCategory() {
        UUID id = UUID.randomUUID();
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setAttributeIds(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
        Category category = new Category();
        when(categoryService.addAttributesToCategory(id, requestDTO.getAttributeIds())).thenReturn(category);

        ResponseEntity<CustomResponse<Category>> response = categoryController.addAttributesToCategory(id, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, Objects.requireNonNull(response.getBody()).getData());
    }

    @Test
    void testRemoveAttributesFromCategory() {
        UUID id = UUID.randomUUID();
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setAttributeIds(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
        Category category = new Category();
        when(categoryService.removeAttributesFromCategory(id, requestDTO.getAttributeIds())).thenReturn(category);

        ResponseEntity<CustomResponse<Category>> response = categoryController.removeAttributesFromCategory(id, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, Objects.requireNonNull(response.getBody()).getData());
    }

    @Test
    void testGetOneCategoryWithoutAttributes() {
        UUID id = UUID.randomUUID();
        GetAllCategoryResponseDto dto = new GetAllCategoryResponseDto();
        dto.setId(id);
        when(categoryService.getOneCategoryWithoutAttributes(id)).thenReturn(Optional.of(dto));

        ResponseEntity<CustomResponse<GetAllCategoryResponseDto>> response = categoryController.getOneCategoryWithoutAttributes(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, Objects.requireNonNull(response.getBody()).getData());
    }
}