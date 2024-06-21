package com.boostech.demo.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteValueByIdDto {
	private UUID productId;
	private UUID attributeId;
}
