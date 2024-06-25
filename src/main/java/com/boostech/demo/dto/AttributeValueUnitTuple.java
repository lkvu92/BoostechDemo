package com.boostech.demo.dto;

import com.boostech.demo.entity.Attribute;
import com.boostech.demo.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValueUnitTuple {
    private Attribute attribute;
    private String value;
    private String status;
}
