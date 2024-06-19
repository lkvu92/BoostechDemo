package com.boostech.demo.repository;

import com.boostech.demo.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IFileRepository extends JpaRepository<File, UUID> {
}
