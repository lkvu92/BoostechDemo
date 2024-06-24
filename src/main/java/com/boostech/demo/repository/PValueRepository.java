package com.boostech.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boostech.demo.entity.PValue;


public interface PValueRepository extends JpaRepository<PValue, UUID> {
	//	Optional<PValue> findByValueId_ProductId(UUID productId);
//	Optional<PValue> findByValueId_AttributeId(UUID attributeId);
	boolean existsByProductIdAndAttributeId(UUID productId, UUID attributeId);
<<<<<<< HEAD

=======
	
>>>>>>> origin/tai-dev
	@Query("select v from PValue v "
			+ "where v.attribute.id = :attributeId "
			+ "and v.product.id = :productId "
			+ "and (:includeDeleted = true or v.deletedAt is null)")
	Optional<PValue> findByProductIdAndAttributeId(
			@Param(value = "productId")
			UUID productId,
			@Param(value = "attributeId")
			UUID attributeId,
			@Param("includeDeleted")
			boolean includeDeleted
	);
<<<<<<< HEAD

=======
	
>>>>>>> origin/tai-dev
	@Query("select v from PValue v join fetch v.product p "
			+ "where v.attribute.id in :attributeIdList "
			+ "and (:includeDeleted = true "
			+ "or v.deletedAt is null)")
	List<PValue> findAllByAttributeIdIn	(
			@Param(value = "attributeIdList")
			List<UUID> attributeIdList,
			@Param("includeDeleted")
			boolean includeDeleted
	);
<<<<<<< HEAD

=======
	
>>>>>>> origin/tai-dev
	@Query("select v from PValue v join fetch v.attribute "
			+ "where v.product.id in :productIdList "
			+ "and (:includeDeleted = true or v.deletedAt is null)")
	List<PValue> findAllByProductIdIn(
			@Param(value = "productIdList")
			List<UUID> productIdList,
			@Param("includeDeleted")
			boolean includeDeleted);
<<<<<<< HEAD

=======
	
>>>>>>> origin/tai-dev
	@Query("select v from PValue v "
			+ "where v.id = :id "
			+ "and (:includeDeleted = true or v.deletedAt is null)")
	Optional<PValue> findById(
<<<<<<< HEAD
			@Param(value = "id")
			UUID id,
			@Param("includeDeleted")
			boolean includeDeleted);

=======
			@Param(value = "id") 
			UUID id,
			@Param("includeDeleted") 
			boolean includeDeleted);
	
>>>>>>> origin/tai-dev
	@Query("select v from PValue v "
			+ "join fetch v.product p join fetch v.attribute a "
			+ "where v.id = :id "
			+ "and (:includeDeleted = true or v.deletedAt is null)")
	Optional<PValue> findByIdWithProductAndAttribute(
<<<<<<< HEAD
			@Param(value = "id") UUID id,
			@Param("includeDeleted")
			boolean includeDeleted);

=======
			@Param(value = "id") UUID id, 
			@Param("includeDeleted") 
			boolean includeDeleted);
	
>>>>>>> origin/tai-dev
//	@Procedure(procedureName = "FILTER_PRODUCT")
//	void filterProduct(Map<String, String> map);
}