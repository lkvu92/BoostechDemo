package com.boostech.demo.repository;

import com.boostech.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findProductsByCategory_Id(UUID categoryId);
    Page<Product> findAllByDeletedAtIsNull(Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> findBySearchTerm(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.deletedAt IS NULL")
    Page<Product> findBySearchTermActive(@Param("name") String name, Pageable pageable);

//    @Query("SELECT p FROM Product p " +
//            "JOIN attribute a ON a.productId = p.id " +
//            "JOIN p_value v ON v.attributeId = a.id " +
//            "JOIN unit u ON u.valueId = v.id " +
//            "WHERE p.categoryId = :categoryId " +
//            "AND ( " +
//            "   (:attributePairs) IS NULL OR " +
//            "   (a.attributeId IN (:attributePairs)) " +
//            ") ")
//    List<Product> findByCategoryIdAndAttributes(UUID categoryId, List<Long> attributePairs);
}
