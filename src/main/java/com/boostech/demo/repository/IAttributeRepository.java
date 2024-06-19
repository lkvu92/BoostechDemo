package com.boostech.demo.repository;

import com.boostech.demo.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface IAttributeRepository extends JpaRepository<Attribute, UUID> {
    //
    //List<Attribute> findAllWithUnit();
}
