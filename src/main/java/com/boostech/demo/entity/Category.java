package com.boostech.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "category")
    private List<CategoryAttribute> categoryAttributes = new ArrayList<>();

    public void addAttribute(Attribute attribute, Boolean isRequired) {
        CategoryAttribute categoryAttribute = new CategoryAttribute();
        categoryAttribute.setCategory(this);
        categoryAttribute.setAttribute(attribute);
        categoryAttribute.setIsRequired(isRequired);
        this.categoryAttributes.add(categoryAttribute);
    }

    public void removeAttribute(Attribute attribute) {
        CategoryAttribute categoryAttribute = this.categoryAttributes.stream()
                .filter(ca -> ca.getAttribute().equals(attribute))
                .findFirst()
                .orElse(null);
        if (categoryAttribute != null) {
            this.categoryAttributes.remove(categoryAttribute);
        }
    }
}