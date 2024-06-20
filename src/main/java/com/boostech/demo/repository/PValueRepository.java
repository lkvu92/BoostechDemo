package com.boostech.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.boostech.demo.entity.PValue;


public interface PValueRepository extends JpaRepository<PValue, UUID> {
//	Optional<PValue> findByValueId_ProductId(UUID productId);
//	Optional<PValue> findByValueId_AttributeId(UUID attributeId);
	
	boolean existsByProductIdAndAttributeId(@Param(value = "productId") UUID productId, @Param(value = "attributeId") UUID attributeId);
	Optional<PValue> findByProductIdAndAttributeId(UUID productId, UUID attributeId);
	Optional<PValue> findByProductIdAndAttributeIdAndDeletedAtIsNull(UUID productId, UUID attributeId);
	List<PValue> findAllByAttributeIdIn(List<UUID> attributeIdList);
	List<PValue> findAllByProductIdIn(List<UUID> productIdList);
	
//	@Procedure(procedureName = "FILTER_PRODUCT")
//	void filterProduct(Map<String, String> map);
}
