package com.boostech.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "unit")
public class Unit extends BaseEntity {
    @Column(name = "name", unique = true, nullable = false)
    private String unitName;

    @Column(name = "unit_type", nullable = false)
    private String unitType;
}
