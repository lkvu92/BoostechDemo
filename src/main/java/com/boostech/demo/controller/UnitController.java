package com.boostech.demo.controller;

import com.boostech.demo.dto.UnitDto;
import com.boostech.demo.entity.Unit;
import com.boostech.demo.service.IUnitService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private static final String UNIT_NOT_FOUND = "Unit not found.";

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UNIT_NOT_FOUND);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> removeUnit(@PathVariable UUID id) {
       try{
           ResponseEntity<?> unit = getUnitDetail(id);
           if(unit == null){
               return ResponseEntity.notFound().build();
           }
           unitService.delete(id);
           return ResponseEntity.ok().body("Removed unit successfully.");
       }catch (EntityNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UNIT_NOT_FOUND);
       }
    }

    @PostMapping()
    public ResponseEntity<?> createUnit(@Valid @RequestBody UnitDto unitDto, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            Unit unit = unitService.create(unitDto);
            return ResponseEntity.ok().body("Created unit successfully.");

        }catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Unit's name already exists.");
        }
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<?> updateUnit(@PathVariable UUID id, @Valid @RequestBody UnitDto unitDto, BindingResult bindingResult) {
        ResponseEntity<?> existingUnit = getUnitDetail(id);
        try{
            if (existingUnit == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UNIT_NOT_FOUND);
            }

            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            Unit unit = unitService.update(id, unitDto);
            return ResponseEntity.ok().body("Updated unit successfully.");

        }catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
