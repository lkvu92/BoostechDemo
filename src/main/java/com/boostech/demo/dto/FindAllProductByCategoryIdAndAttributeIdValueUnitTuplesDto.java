package com.boostech.demo.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllProductByCategoryIdAndAttributeIdValueUnitTuplesDto {
	private UUID categoryId;
	private List<AttributeIdValueUnitTuple> attributeIdValueUnitTuples;
}
