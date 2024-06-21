package com.boostech.demo.service;

import java.util.List;
import java.util.UUID;

import com.boostech.demo.dto.*;
import com.boostech.demo.dto.resDto.FindByProductIdAndAttributeIdResponse;
import com.boostech.demo.entity.PValue;
import com.boostech.demo.entity.Product;

public interface IPValueService {
	PValue findById(UUID id);
	FindByProductIdAndAttributeIdResponse findByProductIdAndAttributeId(DeleteValueByProductIdAndAttributeIdDto dto);
	
//	PValue findByAttributeId(UUID attributeId);
//	
//	PValue findByProductId(UUID productId);
	
	List<PValue> findAllByAttributeIdList(List<UUID> attributeIdList);
	
	List<PValue> findAllByProductIdList(List<UUID> productIdList);
	
	List<Product> findAllProductByCategoryIdAndAttributeIdAndValue(FindAllProductByCategoryIdAndAttributeIdValueUnitTuplesDto dto);

	void createValueById(CreateValueByIdDto dto);
	
	void updateValueById(UpdateValueByIdDto dto);
	
	void updateValueByProductIdAndAttributeId(UpdateValueByProductIdAndAttributeIdDto dto);
	
	boolean deleteValueById(UUID id);
	boolean deleteValueByProductIdAndAttributeId(DeleteValueByProductIdAndAttributeIdDto dto);

	void createValueByProductIdAndAttributeIdValueUnitTuples(Product product, List<AttributeValueUnitTuple> attributeIdValueUnitTuples);
}
