package com.boostech.demo.repository;

import com.boostech.demo.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AttributeRepository extends JpaRepository<Attribute, UUID> {
    List<Attribute> findAllByIdIn(List<UUID> ids);
    @Query("SELECT a FROM Attribute a JOIN a.values v join v.product p WHERE p.id = :productId")
    List<Attribute> findAllByProductId(UUID productId);
}
