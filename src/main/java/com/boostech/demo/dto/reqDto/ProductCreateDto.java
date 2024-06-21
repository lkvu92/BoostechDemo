package com.boostech.demo.dto.reqDto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ProductCreateDto {
    private String name;
    private UUID cate_id;
    private List<AttributeValueDto> attributeValues = new ArrayList<>();
}
