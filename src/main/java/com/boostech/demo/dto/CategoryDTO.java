package com.boostech.demo.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CategoryDTO {
    private String name;
    private List<UUID> attributeIds;
}
