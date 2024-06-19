package com.boostech.demo.repository;

import com.boostech.demo.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @EntityGraph(attributePaths = {"attributes"})
    Optional<Category> findById(UUID id);

}
