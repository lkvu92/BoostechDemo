package com.boostech.demo.service;

import com.boostech.demo.dto.reqDto.CategoryRequestDTO;
import com.boostech.demo.dto.resDto.CategoryResponseDto;
import com.boostech.demo.entity.Attribute;
import com.boostech.demo.entity.Category;
import com.boostech.demo.exception.attribute.AttributeAlreadyAddedException;
import com.boostech.demo.exception.attribute.AttributeNotFoundException;
import com.boostech.demo.exception.category.CategoryNotFoundException;
import com.boostech.demo.repository.AttributeRepository;
import com.boostech.demo.repository.CategoryRepository;
import com.boostech.demo.util.CategoryToCategoryResponseDtoConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final CategoryToCategoryResponseDtoConverter converter;

    private static final String CATEGORY_NOT_FOUND = "Category not found";

    public CategoryService(CategoryRepository categoryRepository, AttributeRepository attributeRepository, CategoryToCategoryResponseDtoConverter converter) {
        this.categoryRepository = categoryRepository;
        this.attributeRepository = attributeRepository;
        this.converter = converter;
    }

    /**
     * Get all categories
     * @return
     */
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponseDto dto = new CategoryResponseDto();
            dto.setId(category.getId());
            dto.setCreatedAt(category.getCreatedAt());
            dto.setUpdatedAt(category.getUpdatedAt());
            dto.setDeletedAt(category.getDeletedAt());
            dto.setName(category.getName());
            categoryDtos.add(dto);
        }
        return categoryDtos;
    }

    /**
     * Get category by id
     * @param id UUID
     * @return
     */
    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    public Optional<CategoryResponseDto> getOneCategoryWithoutAttributes(UUID id) {
        Optional<Category> category = categoryRepository.getOneWithoutAttributes(id);
        return category.map(converter::convert);
    }

    /**
     * Create a new category
     * @param categoryRequestDTO
     * @return
     */
    public Category createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        return categoryRepository.save(category);
    }

    /**
     * Update a category
     * @param id
     * @param categoryRequestDTO
     * @return
     */
    public Category updateCategory(UUID id, CategoryRequestDTO categoryRequestDTO) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            category.setName(categoryRequestDTO.getName());
            return categoryRepository.save(category);
        } else {
            throw new CategoryNotFoundException(CATEGORY_NOT_FOUND);
        }
    }


    /**
     * Delete a category
     * @param id UUID
     */
    public void deleteCategory(UUID id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            if (category.getDeletedAt() != null) {
                throw new CategoryNotFoundException("Category already deleted");
            }
            category.setDeletedAt(LocalDateTime.now());
            categoryRepository.save(category);
        } else {
            throw new CategoryNotFoundException(CATEGORY_NOT_FOUND);
        }
    }

    /**
     * Add attributes to a category
     * @param categoryId UUID
     * @param attributeIds List of UUID
     * @return
     */
    @Transactional
    public Category addAttributesToCategory(UUID categoryId, List<UUID> attributeIds) {
        return modifyAttributesInCategory(categoryId, attributeIds, true);
    }

    /**
     * Remove attributes from a category
     * @param categoryId UUID
     * @param attributeIds List of UUID
     * @return
     */
    @Transactional
    public Category removeAttributesFromCategory(UUID categoryId, List<UUID> attributeIds) {
        return modifyAttributesInCategory(categoryId, attributeIds, false);
    }

    /**
     * Modify attributes in category
     * @param categoryId
     * @param attributeIds
     * @param isAdding
     * @return
     */
    private Category modifyAttributesInCategory(UUID categoryId, List<UUID> attributeIds, boolean isAdding) {
        Category category = getCategoryOrThrow(categoryId);
        attributeIds.forEach(attributeId -> modifyAttributeInCategory(category, attributeId, isAdding));
        return categoryRepository.save(category);
    }

    /**
     * Modify attribute in category
     * @param category
     * @param attributeId
     * @param isAdding
     */
    private void modifyAttributeInCategory(Category category, UUID attributeId, boolean isAdding) {
        Attribute attribute = getAttributeOrThrow(attributeId);
        if (isAdding) {
            if (category.getAttributes().contains(attribute)) {
                throw new AttributeAlreadyAddedException("Attribute with ID " + attributeId +" - "+ " already added to the category");
            }
            category.addAttribute(attribute);
        } else {
            if (!category.getAttributes().contains(attribute)) {
                throw new AttributeNotFoundException("Attribute with ID " + attributeId +" - "+ " not found in the category");
            }
            category.removeAttribute(attribute);
        }
    }

    /**
     * Get category by id or throw exception
     * @param categoryId UUID
     * @return
     */
    private Category getCategoryOrThrow(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND));
    }

    /**
     * Get attribute by id or throw exception
     * @param attributeId
     * @return
     */
    private Attribute getAttributeOrThrow(UUID attributeId) {
        return attributeRepository.findById(attributeId)
                .orElseThrow(() -> new RuntimeException("Attribute not found"));
    }
}
