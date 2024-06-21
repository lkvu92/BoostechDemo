package com.boostech.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "unit")
public class Unit extends BaseEntity {
    @Column(name = "name", unique = true, nullable = false)
    private String unitName;

    @Column(name = "unit_type", nullable = false)
    private String unitType;

    @ManyToMany
    @JoinTable(
            name = "attribute_unit",
            joinColumns = @JoinColumn(name = "unit_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private List<Attribute> attributes = new ArrayList<>();
}
