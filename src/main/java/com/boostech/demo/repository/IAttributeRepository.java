package com.boostech.demo.repository;

import com.boostech.demo.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IAttributeRepository extends JpaRepository<Attribute, UUID> {
}
