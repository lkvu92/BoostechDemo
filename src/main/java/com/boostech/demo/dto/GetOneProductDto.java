package com.boostech.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOneProductDto {
    private UUID id;
    private String name;
    private CategoryDto category;
    private List<AttributeDto> attributes = new ArrayList<>();

        @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryDto {
        private UUID id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttributeDto {
        private UUID id;
        private String name;
        private String value;
        private String unit;
    }
}
