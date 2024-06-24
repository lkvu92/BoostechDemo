package com.boostech.demo.service;

import com.boostech.demo.dto.UnitDto;
import com.boostech.demo.entity.*;
import com.boostech.demo.repository.IUnitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnitService implements IUnitService {
    private final IUnitRepository repository;

    public List<Unit> getAll() {
        try{
            return repository.findAll();
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public Unit getById(UUID id) {
        Optional<Unit> unitOptional = repository.findById(id);

        if (unitOptional.isEmpty()) {
            throw new EntityNotFoundException("Unit with id " + id + " not found");
        }

        return unitOptional.get();
    }

    public Unit create(UnitDto unitDto) {
        try {
            Unit unit = new Unit();
            unit.setUnitName(unitDto.getUnitName());
            unit.setUnitType(unitDto.getUnitType());

            return repository.save(unit);
        }catch (Exception e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public Unit update(UUID id, UnitDto unitDto) {
       try {
           Unit existingUnit = getById(id);
           if (existingUnit == null) {
               throw new EntityNotFoundException("Unit with id " + id + " not found");
           }

           if(existingUnit.getUnitName().equals(unitDto.getUnitName())) {
               existingUnit.setUnitName(existingUnit.getUnitName());
           }

           existingUnit.setUnitType(unitDto.getUnitType());
           existingUnit.setDeletedAt(LocalDateTime.now());
           return repository.save(existingUnit);

       }catch (Exception e){
           throw new EntityNotFoundException(e.getMessage());
       }
    }

    public void delete(UUID id) {
        Unit existingUnit = getById(id);
        if (existingUnit == null) {
            throw new EntityNotFoundException("Unit with id " + id + " not found");
        }

        existingUnit.setDeletedAt(LocalDateTime.now());
        repository.delete(existingUnit);
    }
}
