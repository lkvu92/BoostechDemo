package com.boostech.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitDto {
    @NotBlank(message = "Unit's name is required.")
    private String unitName;

    @NotBlank(message = "Unit's type is required.")
    private String unitType;

    private boolean isActive = true;
}
