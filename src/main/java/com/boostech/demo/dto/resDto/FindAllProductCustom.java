package com.boostech.demo.dto.resDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllProductCustom {
    private UUID id;
    private String name;
    private CategoryCustom category;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryCustom{
        private UUID id;
        private String name;
    }
}
