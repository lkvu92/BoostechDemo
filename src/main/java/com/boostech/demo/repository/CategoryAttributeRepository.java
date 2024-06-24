package com.boostech.demo.repository;

import com.boostech.demo.entity.CategoryAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute, UUID>{
}
