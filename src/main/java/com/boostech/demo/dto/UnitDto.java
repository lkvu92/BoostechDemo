package com.boostech.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitDto {
    @NotBlank(message = "unitType is required.")
    private String unitType;
    private boolean isActive = true;
}
