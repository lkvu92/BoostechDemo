package com.boostech.demo.repository;

import com.boostech.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findProductsByCategory_Id(UUID categoryId);
}
