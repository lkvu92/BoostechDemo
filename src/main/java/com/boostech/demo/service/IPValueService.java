package com.boostech.demo.service;

import java.util.List;
import java.util.UUID;

import com.boostech.demo.dto.CreateValueByIdDto;
import com.boostech.demo.dto.DeleteValueByProductIdAndAttributeIdDto;
import com.boostech.demo.dto.FindAllProductByCategoryIdAndAttributeIdValueUnitTuplesDto;
import com.boostech.demo.dto.UpdateValueByIdDto;
import com.boostech.demo.dto.UpdateValueByProductIdAndAttributeIdDto;
import com.boostech.demo.entity.PValue;
import com.boostech.demo.entity.Product;

public interface IPValueService {
	PValue findById(UUID id);
	PValue findByProductIdAndAttributeId(DeleteValueByProductIdAndAttributeIdDto dto);
	
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
}
