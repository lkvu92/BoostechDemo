package com.boostech.demo.dto.reqDto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AddAttributeToCategoryDto {
    @Data
    public static class Attribute {
        private UUID id;
        private Boolean isRequired;
    }
    private List<Attribute> attributes;
}
