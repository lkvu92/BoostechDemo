package com.boostech.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attribute")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIdentityReference(alwaysAsId = true)
public class Attribute extends BaseEntity {
    @Column(name = "name", unique = false, nullable = false)
    private String attributeName;

    @OneToOne()
    @JoinColumn(name = "unit_id", referencedColumnName = "id", nullable = true)
    private Unit unit;

    @ManyToMany(mappedBy = "attributes")
    private List<Category> categories = new ArrayList<>();
}
