package com.boostech.demo.service;

import com.boostech.demo.dto.AttributeDto;
import com.boostech.demo.entity.Attribute;

import java.util.List;
import java.util.UUID;

public interface IAttributeService {
//    List<Attribute> findAllWithUnit();
    List<Attribute> getAll();
    Attribute getById(UUID id);
    Attribute create(AttributeDto attributeDto);
    Attribute update(UUID id, AttributeDto attributeDto);
    void delete(UUID id);
}
