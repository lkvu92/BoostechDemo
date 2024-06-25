package com.boostech.demo.repository;

import com.boostech.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("select p from Product p join fetch p.category c join fetch p.values v join fetch v.attribute a join fetch a.unit where p.id = :id")
    Optional<Product> findByIdWithCategoryAndAttributes(@Param(value = "id") UUID id);

    List<Product> findProductsByCategory_Id(UUID categoryId);
    Page<Product> findAllByDeletedAtIsNull(Pageable pageable);
    boolean existsByName(String name);

    @Query(value = "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> findBySearchTerm(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.deletedAt IS NULL")
    Page<Product> findBySearchTermActive(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT * FROM Product p JOIN PValue v ON v.product_id = p.id WHERE v.category_id = :categoryId AND (:includeDeleted = true OR p.deletedAt IS NULL)", nativeQuery = true)
    Page<Product> findAllProductByCategoryIdAndAttributeIdAndValue(@Param("categoryId") Long categoryId, @Param("includeDeleted") boolean includeDeleted, Pageable pageable);
}
