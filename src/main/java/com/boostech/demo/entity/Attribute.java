package com.boostech.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attribute")
public class Attribute extends BaseEntity {
    @Column(name = "name", unique = true, nullable = false)
    private String attributeName;

    @OneToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;
}
