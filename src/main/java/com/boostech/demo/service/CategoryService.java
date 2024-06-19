package com.boostech.demo.service;

import com.boostech.demo.dto.CategoryDTO;
import com.boostech.demo.entity.Attribute;
import com.boostech.demo.entity.Category;
import com.boostech.demo.repository.AttributeRepository;
import com.boostech.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
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
     * Create a new category
     * @param categoryDTO
     * @return
     */
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    /**
     * Update a category
     * @param id
     * @param categoryDTO
     * @return
     */
    public Category updateCategory(UUID id, CategoryDTO categoryDTO) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            category.setName(categoryDTO.getName());
            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found");
        }
    }


    /**
     * Delete a category
     * @param id
     */
    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Add attributes to a category
     * @param categoryId UUID
     * @param attributeIds [attributeId1, attributeId2, ...]
     * @return
     */
    @Transactional
    public Category addAttributesToCategory(UUID categoryId, List<UUID> attributeIds) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            for (UUID attributeId : attributeIds) {
                Optional<Attribute> attributeOpt = attributeRepository.findById(attributeId);
                if (attributeOpt.isPresent()) {
                    Attribute attribute = attributeOpt.get();

                    // Add the attribute to the category if it is not already present
                    if (!category.getAttributes().contains(attribute)) {
                        category.addAttribute(attribute);
                    }
                }
            }
            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    /**
     * Remove attributes from a category
     * @param categoryId
     * @param attributeIds
     * @return
     */
    @Transactional
    public Category removeAttributesFromCategory(UUID categoryId, List<UUID> attributeIds) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            for (UUID attributeId : attributeIds) {
                Optional<Attribute> attributeOpt = attributeRepository.findById(attributeId);
                attributeOpt.ifPresent(category::removeAttribute);
            }
            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found");
        }
    }
}
