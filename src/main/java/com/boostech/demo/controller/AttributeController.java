package com.boostech.demo.controller;

import com.boostech.demo.dto.AttributeDto;
import com.boostech.demo.entity.Attribute;
import com.boostech.demo.service.AttributeService;
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
@RequestMapping("api/v1/attributes")
public class AttributeController {

    private final AttributeService attributeService;

    @GetMapping()
    public ResponseEntity<?> getAllAttribute() {
        List<Attribute> attributeList = attributeService.getAll();
        return ResponseEntity.ok(attributeList);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getAttributeDetail(@PathVariable UUID id) {
        Attribute existingAttribute = attributeService.getById(id);
        if (existingAttribute == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(existingAttribute);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> removeAttribute(@PathVariable UUID id) {
        ResponseEntity<?> existingAttribute = getAttributeDetail(id);
        if (existingAttribute == null) {
            return ResponseEntity.notFound().build();
        }
        attributeService.delete(id);
        return ResponseEntity.ok().body("Removed Attribute Successfully.");
    }

    @PostMapping()
    public ResponseEntity<?> createAttribute(@Valid @RequestBody AttributeDto attributeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            List<String> errorMessages = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }

        attributeService.create(attributeDto);
        return ResponseEntity.ok(attributeDto);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<?> updateAttribute(@Valid @RequestBody AttributeDto attributeDto, @PathVariable UUID id, BindingResult bindingResult) {
        ResponseEntity<?> existingAttribute = getAttributeDetail(id);
        if (existingAttribute != null) {
           if(bindingResult.hasErrors()){
               List<String> errorMessages = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
               return ResponseEntity.badRequest().body(errorMessages);
           }

           attributeService.update(id, attributeDto);
           return ResponseEntity.ok().body("Attribute Updated Successfully.");
        }
        return ResponseEntity.notFound().build();
    }
}
