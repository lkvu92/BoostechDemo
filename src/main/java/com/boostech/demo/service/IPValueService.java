package com.boostech.demo.service;

import java.util.List;
import java.util.UUID;

import com.boostech.demo.dto.*;
import com.boostech.demo.dto.resDto.FindAllProductByCategoryIdAndAttributeIdAndValueResponse;
import com.boostech.demo.dto.resDto.FindPValueByIdResponse;
import com.boostech.demo.dto.resDto.FindPValueByProductIdAndAttributeIdResponse;
import com.boostech.demo.dto.resDto.FindPValuesByAttributeIdList;
import com.boostech.demo.dto.resDto.FindPValuesByProductIdList;
import com.boostech.demo.entity.Product;

public interface IPValueService {
	FindPValueByIdResponse findById(UUID id);
	FindPValueByProductIdAndAttributeIdResponse findByProductIdAndAttributeId(DeleteValueByProductIdAndAttributeIdDto dto);
	
//	PValue findByAttributeId(UUID attributeId);
//	
//	PValue findByProductId(UUID productId);
	
	List<FindPValuesByAttributeIdList> findAllByAttributeIdList(List<UUID> attributeIdList);
	
	List<FindPValuesByProductIdList> findAllByProductIdList(List<UUID> productIdList);
	
	List<FindAllProductByCategoryIdAndAttributeIdAndValueResponse> findAllProductByCategoryIdAndAttributeIdAndValue(FindAllProductByCategoryIdAndAttributeIdValueUnitTuplesDto dto);

	void createValueById(CreateValueByIdDto dto);
	
	void updateValueById(UpdateValueByIdDto dto);
	
	void updateValueByProductIdAndAttributeId(UpdateValueByProductIdAndAttributeIdDto dto);
	
	boolean deleteValueById(UUID id);
	boolean deleteValueByProductIdAndAttributeId(DeleteValueByProductIdAndAttributeIdDto dto);

	void createValueByProductIdAndAttributeIdValueUnitTuples(Product product, List<AttributeValueUnitTuple> attributeIdValueUnitTuples);
}
