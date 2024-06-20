package com.boostech.demo.service;

import java.util.List;
import java.util.UUID;

import com.boostech.demo.dto.CreateValueByIdDto;
import com.boostech.demo.dto.DeleteValueByIdDto;
import com.boostech.demo.dto.FindAllProductByCategoryIdAndAttributeIdValuePairsDto;
import com.boostech.demo.entity.PValue;
import com.boostech.demo.entity.Product;

public interface IPValueService {
	PValue findByProductIdAndAttributeId(DeleteValueByIdDto dto);
	
//	PValue findByAttributeId(UUID attributeId);
//	
//	PValue findByProductId(UUID productId);
	
	List<PValue> findAllByAttributeIdList(List<UUID> attributeIdList);
	
	List<PValue> findAllByProductIdList(List<UUID> productIdList);
	
	List<Product> findAllProductByCategoryIdAndAttributeIdAndValue(FindAllProductByCategoryIdAndAttributeIdValuePairsDto dto);

	void createValueById(CreateValueByIdDto dto);
	
	void updateValueById(CreateValueByIdDto dto);
	
	void deleteValueById(DeleteValueByIdDto dto);
}
