package com.boostech.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {
    private String name;

    @ElementCollection
    @CollectionTable(name = "Attribute", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "attributes")
    private List<UUID> attributes;
}