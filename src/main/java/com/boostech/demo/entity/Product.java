package com.boostech.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "Product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {
    @Column(name = "name", length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "cate_id")
    private Category category;
}
