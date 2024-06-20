package com.boostech.demo.controller;

import com.boostech.demo.dto.UnitDto;
import com.boostech.demo.entity.Unit;
import com.boostech.demo.service.IUnitService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/units")
public class UnitController {
    private final IUnitService unitService;

    @GetMapping()
    public ResponseEntity<?> getAllUnits(){
        List<Unit> unitList = unitService.getAll();
        return ResponseEntity.ok(unitList);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUnitDetail(@PathVariable UUID id) {
        try {
            Unit unit = unitService.getById(id);
            return ResponseEntity.ok().body(unit);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> removeUnit(@PathVariable UUID id) {
        ResponseEntity<?> unit = getUnitDetail(id);
        if(unit == null){
            return ResponseEntity.notFound().build();
        }
        unitService.delete(id);
        return ResponseEntity.ok().body("Removed unit successfully.");
    }

    @PostMapping()
    public ResponseEntity<?> createUnit(@Valid  @RequestBody UnitDto unitDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }

        Unit unit = unitService.create(unitDto);

        return ResponseEntity.ok(unit);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<?> updateUnit(@PathVariable UUID id, @Valid @RequestBody UnitDto unitDto, BindingResult bindingResult) {
        ResponseEntity<?> existingUnit = getUnitDetail(id);
        if (existingUnit == null) {
            return ResponseEntity.notFound().build();
        }

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }

        Unit unit = unitService.update(id, unitDto);
        return ResponseEntity.ok(unit);
    }
}
