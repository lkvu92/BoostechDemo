package com.boostech.demo.dto.reqDto.category;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RemoveAttributeFromCategoryDto {
    private List<UUID> attributeIds;
}
