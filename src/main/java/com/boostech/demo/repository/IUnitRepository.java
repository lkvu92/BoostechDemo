package com.boostech.demo.repository;

import com.boostech.demo.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUnitRepository extends JpaRepository<Unit, UUID> {
	
}
