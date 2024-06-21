package com.boostech.demo.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateValueByIdDto {
	private UUID id;
	private String value;
	private UUID attributeId;
	private UUID productId;
	private UUID unitId;
}
