package com.boostech.demo.dto.resDto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPValuesByAttributeIdList {
	private UUID id;
	private UUID productId;
	private String value;
	private String type;
	private String unitName;
}
