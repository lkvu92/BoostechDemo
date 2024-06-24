package com.boostech.demo.service;

import com.boostech.demo.dto.reqDto.AddAttributeToCategoryDto;
import com.boostech.demo.dto.reqDto.category.CategoryRequestDTO;
import com.boostech.demo.dto.reqDto.category.RemoveAttributeFromCategoryDto;
import com.boostech.demo.dto.resDto.AttributeResponseDto;
import com.boostech.demo.dto.resDto.category.GetAllCategoryResponseDto;
import com.boostech.demo.entity.Attribute;
import com.boostech.demo.entity.Category;
import com.boostech.demo.entity.CategoryAttribute;
import com.boostech.demo.exception.attribute.AttributeAlreadyAddedException;
import com.boostech.demo.exception.attribute.AttributeNotFoundException;
import com.boostech.demo.exception.category.CategoryNotFoundException;
import com.boostech.demo.repository.AttributeRepository;
import com.boostech.demo.repository.CategoryAttributeRepository;
import com.boostech.demo.repository.CategoryRepository;
import com.boostech.demo.util.CategoryToCategoryResponseDtoConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final CategoryToCategoryResponseDtoConverter converter;
    private final CategoryAttributeRepository categoryAttributeRepository;
    private final PValueService pValueService;
    private static final String CATEGORY_NOT_FOUND = "Category not found";


    public CategoryService(CategoryRepository categoryRepository, CategoryAttributeRepository  categoryAttributeRepository,AttributeRepository attributeRepository, CategoryToCategoryResponseDtoConverter converter, PValueService pValueService) {
        this.categoryRepository = categoryRepository;
        this.categoryAttributeRepository = categoryAttributeRepository;
        this.attributeRepository = attributeRepository;
        this.converter = converter;
        this.pValueService = pValueService;
    }

    /**
     * Get all categories
     * @return
     */
    public List<GetAllCategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<GetAllCategoryResponseDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            GetAllCategoryResponseDto dto = new GetAllCategoryResponseDto();
            dto.setId(category.getId());
            dto.setCreatedAt(category.getCreatedAt());
            dto.setUpdatedAt(category.getUpdatedAt());
            dto.setDeletedAt(category.getDeletedAt());
            dto.setName(category.getName());

            // Add the attributes to the response
            List<AttributeResponseDto> attributeDtos = new ArrayList<>();
            for (CategoryAttribute categoryAttribute : category.getCategoryAttributes()) {
                Attribute attribute = categoryAttribute.getAttribute();
                AttributeResponseDto attributeDto = new AttributeResponseDto();
                attributeDto.setId(attribute.getId());
                attributeDto.setName(attribute.getAttributeName());
                attributeDto.setIsRequired(categoryAttribute.getIsRequired());
                attributeDtos.add(attributeDto);
            }
//            dto.setAttributes(attributeDtos);

            categoryDtos.add(dto);
        }
        return categoryDtos;
    }

    /**
     * Get category by id
     * @param id
     * @return
     */
    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    /**
     * Get category by id without attributes
     * @param id
     * @return
     */
    public Optional<GetAllCategoryResponseDto> getOneCategoryWithoutAttributes(UUID id) {
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

    @Transactional
    public void addAttributeToCategory(UUID categoryId, AddAttributeToCategoryDto dto) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isEmpty()) {
            throw new CategoryNotFoundException(CATEGORY_NOT_FOUND);
        }
        Category category = categoryOpt.get();
        Map<UUID, Boolean> map = new HashMap<>();
        for (AddAttributeToCategoryDto.Attribute attribute : dto.getAttributes()) {
            map.put(attribute.getId(), attribute.getIsRequired());
        }

        List<Attribute> attributes = attributeRepository.findAllByIdIn(map.keySet().stream().toList());

        if (attributes.size() != map.size()) {
            throw new AttributeNotFoundException("Attribute not found");
        }

        Map<UUID, Attribute> map1 = new HashMap<>();
        for (Attribute attribute : attributes) {
            map1.put(attribute.getId(), attribute);
        }

        for (AddAttributeToCategoryDto.Attribute attribute : dto.getAttributes()) {
            // Check if the attribute already exists in the category
            boolean attributeExists = category.getCategoryAttributes().stream()
                    .anyMatch(ca -> ca.getAttribute().getId().equals(attribute.getId()));
            if (attributeExists) {
                throw new AttributeAlreadyAddedException("Attribute already exists in the category");
            }

            CategoryAttribute categoryAttribute =  new CategoryAttribute();
            categoryAttribute.setCategory(category);
            categoryAttribute.setIsRequired(map.get(attribute.getId()));
            categoryAttribute.setAttribute(map1.get(attribute.getId()));

            categoryAttributeRepository.save(categoryAttribute);
            category.getCategoryAttributes().add(categoryAttribute);
        }

        categoryRepository.save(category);
    }

    @Transactional
    public void removeAttributesFromCategory(UUID categoryId, RemoveAttributeFromCategoryDto dto) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isEmpty()) {
            throw new CategoryNotFoundException(CATEGORY_NOT_FOUND);
        }
        Category category = categoryOpt.get();
        for (UUID attributeId : dto.getAttributeIds()) {
            Optional<CategoryAttribute> categoryAttributeOpt = category.getCategoryAttributes().stream()
                    .filter(ca -> ca.getAttribute().getId().equals(attributeId))
                    .findFirst();
            if (categoryAttributeOpt.isEmpty()) {
                throw new AttributeNotFoundException("Attribute not found");
            }
            CategoryAttribute categoryAttribute = categoryAttributeOpt.get();
            category.getCategoryAttributes().remove(categoryAttribute);
            categoryAttributeRepository.delete(categoryAttribute);
        }
        categoryRepository.save(category);
    }
}
