package com.boostech.demo.service;

import java.util.List;
import java.util.UUID;

import com.boostech.demo.dto.*;
import com.boostech.demo.dto.resDto.FindAllProductByCategoryIdAndAttributeIdAndValueResponse;
import com.boostech.demo.dto.resDto.FindPValueByIdResponse;
import com.boostech.demo.dto.resDto.FindPValueByProductIdAndAttributeIdResponse;
import com.boostech.demo.dto.resDto.FindPValuesByAttributeIdList;
import com.boostech.demo.dto.resDto.FindPValuesByProductIdList;
import com.boostech.demo.entity.Category;
import com.boostech.demo.entity.Product;

public interface IPValueService {
	FindPValueByIdResponse findById(UUID id, boolean includeDeleted);
	FindPValueByProductIdAndAttributeIdResponse findByProductIdAndAttributeId(
			DeleteValueByProductIdAndAttributeIdDto dto, 
			boolean includeDeleted
	);
	
	List<FindPValuesByAttributeIdList> findAllByAttributeIdList(List<UUID> attributeIdList, boolean includeDeleted);
	
	List<FindPValuesByProductIdList> findAllByProductIdList(List<UUID> productIdList, boolean includeDeleted);
	
	List<FindAllProductByCategoryIdAndAttributeIdAndValueResponse> findAllProductByCategoryIdAndAttributeIdAndValue(
			FindAllProductByCategoryIdAndAttributeIdValueUnitTuplesDto dto, boolean includeDeleted
	);

	void createValueById(CreateValueByIdDto dto);
	
	void updateValueById(UpdateValueByIdDto dto);
	
	void updateValueByProductIdAndAttributeId(UpdateValueByProductIdAndAttributeIdDto dto);
	
	boolean deleteValueById(UUID id);
	boolean deleteValueByProductIdAndAttributeId(DeleteValueByProductIdAndAttributeIdDto dto);

	void createValueByProductIdAndAttributeIdValueUnitTuples(Product product, Category category, List<AttributeValueUnitTuple> attributeIdValueUnitTuples);

	void updateValueByProductIdAndAttributeIdValueUnitTuples(Product product, List<UpdateValueByProductIdAndAttributeIdDto> updateValueByProductIdAndAttributeIdDtos);
	void update(Product product, Category category, List<AttributeValueUnitTuple> attributeIdValueUnitTuples);
}
