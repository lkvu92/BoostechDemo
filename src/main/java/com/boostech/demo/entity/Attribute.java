package com.boostech.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public class Attribute extends BaseEntity{
    @Column(name = "name", unique = true, nullable = false)
    private String attributeName;

    @OneToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;
}
