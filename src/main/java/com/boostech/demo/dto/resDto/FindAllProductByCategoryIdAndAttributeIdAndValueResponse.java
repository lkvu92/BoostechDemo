package com.boostech.demo.dto.resDto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllProductByCategoryIdAndAttributeIdAndValueResponse {
	private UUID id;
	private String name;
}
