package com.boostech.demo.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllProductByCategoryIdAndAttributeIdValuePairsDto {
	private UUID categoryId;
	private List<AttributeIdValuePair> attributeIdValuePairs;
}
