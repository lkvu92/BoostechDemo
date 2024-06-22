package com.boostech.demo.dto;

import com.boostech.demo.entity.Attribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValueUnitTuple {
    private Attribute attribute;
    private String value;
}
