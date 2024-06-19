package com.boostech.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files")
public class File extends BaseEntity {
    @Column(name = "source_id", nullable = false, unique = true)
    private UUID sourceId;

    @Column(name = "file_path", nullable = false)
    private String filePath;
}
