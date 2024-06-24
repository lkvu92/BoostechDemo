package com.boostech.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "category_attribute")
@AllArgsConstructor
@NoArgsConstructor

public class CategoryAttribute extends BaseEntity{
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;

    @Column(name = "is_required")
    private Boolean isRequired;

    @Transient
    private UUID categoryId;

    @Transient
    private UUID attributeId;

    @PostLoad
    void fillTransient() {
        if (category != null) {
            this.categoryId = category.getId();
        }
        if (attribute != null) {
            this.attributeId = attribute.getId();
        }
    }
}
