package com.boostech.demo.dto;

import com.boostech.demo.dto.reqDto.AttributeValueDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductUpdateDto {
    private String name;
    private List<AttributeValueDto> attributeValues = new ArrayList<>();
}
