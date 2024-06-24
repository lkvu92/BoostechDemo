package com.boostech.demo.dto.resDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeResponseDto {
    private UUID id;
    private String name;
    private Boolean isRequired;
}
