package com.boostech.demo.repository;

import com.boostech.demo.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttributeRepository extends JpaRepository<Attribute, UUID> {
    List<Attribute> findAllByIdIn(List<UUID> ids);
}
