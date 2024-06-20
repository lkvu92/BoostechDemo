package com.boostech.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boostech.demo.entity.PValue;
import com.boostech.demo.entity.PValuePrimaryKey;


public interface PValueRepository extends JpaRepository<PValue, PValuePrimaryKey> {
//	Optional<PValue> findByValueId_ProductId(UUID productId);
//	Optional<PValue> findByValueId_AttributeId(UUID attributeId);
	List<PValue> findAllByValueId_AttributeIdIn(List<UUID> attributeIdList);
	List<PValue> findAllByValueId_ProductIdIn(List<UUID> productIdList);
	
//	@Procedure(procedureName = "FILTER_PRODUCT")
//	void filterProduct(Map<String, String> map);
}
