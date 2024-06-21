package com.boostech.demo.service;

import com.boostech.demo.dto.AttributeDto;
import com.boostech.demo.entity.Attribute;
import com.boostech.demo.entity.Unit;
import com.boostech.demo.repository.IAttributeRepository;
import com.boostech.demo.repository.IUnitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class AttributeService implements IAttributeService {

    private final IAttributeRepository repository;
    @Autowired
    private IUnitRepository unitRepository;

    public List<Attribute> getAll() {
        return repository.findAll();
    }

    public Attribute getById(UUID id) {
        Optional<Attribute> attribute = repository.findById(id);
        if(attribute.isEmpty()){
            throw new EntityNotFoundException("Attribute not found.");
        }

        return attribute.get();
    }

    public Attribute create(AttributeDto attributeDto) {
        try {
            Attribute attribute = new Attribute();
            attribute.setAttributeName(attributeDto.getAttributeName());

            Unit unit = unitRepository.findById(attributeDto.getUnitId()).orElseThrow();
            List<Unit> units = new ArrayList<>();
            units.add(unit);
            attribute.setUnits(units);

            return repository.save(attribute);
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public Attribute update(UUID id, AttributeDto attributeDto) {
        try {
            Attribute existingAttribute = getById(id);
            if(existingAttribute == null){
                throw new EntityNotFoundException("Attribute not found.");
            }

            existingAttribute.setAttributeName(attributeDto.getAttributeName());
            Unit unit = unitRepository.findById(attributeDto.getUnitId()).orElseThrow();

            List<Unit> unitList = new ArrayList<>();
            unitList.add(unit);

            existingAttribute.getUnits().add(unit); // Thêm Unit mới
            return repository.save(existingAttribute);

        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public void delete(UUID id) {
        Attribute existingAttribute = getById(id);
        if(existingAttribute == null){
            throw new EntityNotFoundException("Attribute not found.");
        }

        existingAttribute.setDeletedAt(LocalDateTime.now());
        repository.delete(existingAttribute);
    }
}
