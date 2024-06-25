package com.boostech.demo.dto;

import com.boostech.demo.entity.Attribute;
import lombok.Data;

@Data
public class AttributeWithStatusDto {
    private Attribute attribute;
    private String status;
}
