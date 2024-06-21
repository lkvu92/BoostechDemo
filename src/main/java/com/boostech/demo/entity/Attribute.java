package com.boostech.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

public class Attribute extends BaseEntity {
    @Column(name = "name", unique = false, nullable = false)
    private String attributeName;

    @JsonBackReference
    @ManyToMany(mappedBy = "attributes")
    private List<Category> categories = new ArrayList<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "attributes")
    private List<Unit> units = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "attribute")
    List<PValue> values = new ArrayList<>();
}
