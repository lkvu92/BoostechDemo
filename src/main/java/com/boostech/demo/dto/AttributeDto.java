package com.boostech.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeDto {
    @NotBlank(message = "Attribute is required.")
    private String attributeName;
    private UUID unitId;
    private boolean isActive = true;
}
