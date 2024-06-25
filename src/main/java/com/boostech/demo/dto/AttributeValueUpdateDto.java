package com.boostech.demo.dto;

import com.boostech.demo.entity.Attribute;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class AttributeValueUpdateDto {
    private Map<UUID, Attribute> create;
    private Map<UUID, Attribute> update;
}