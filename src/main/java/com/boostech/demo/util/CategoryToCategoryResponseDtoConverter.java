package com.boostech.demo.util;

import com.boostech.demo.dto.resDto.CategoryResponseDto;
import com.boostech.demo.entity.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryResponseDtoConverter implements Converter<Category, CategoryResponseDto> {

    @Override
    public CategoryResponseDto convert(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        dto.setDeletedAt(category.getDeletedAt());
        dto.setName(category.getName());
        return dto;
    }
}