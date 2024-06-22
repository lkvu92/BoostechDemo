package com.boostech.demo.service.seeder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class SeederAttributeService {
    @PersistenceContext
    private EntityManager entityManager;


    public String attributeSeeds() {
        try {
            List<AttributeType> products = new ArrayList<>();
            products.add(new AttributeType(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Attribute name 1","00000000-0000-0000-0000-000000000001"));
            products.add(new AttributeType(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Attribute name 2", "00000000-0000-0000-0000-000000000002"));
            products.add(new AttributeType(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Attribute name 3", "00000000-0000-0000-0000-000000000003"));
            products.add(new AttributeType(UUID.fromString("00000000-0000-0000-0000-000000000004"), "Attribute name 4", "00000000-0000-0000-0000-000000000004"));
            products.add(new AttributeType(UUID.fromString("00000000-0000-0000-0000-000000000005"), "Attribute name 5", "00000000-0000-0000-0000-000000000005"));
            for (AttributeType product : products) {
                entityManager.createNativeQuery("INSERT INTO attribute (id, name, unit_id) VALUES (?, ?, ?)")
                        .setParameter(1, product.getId())
                        .setParameter(2, product.getName())
                        .setParameter(3, product.getUnit_id())
                        .executeUpdate();
            }
            return "Attribute table Seeded";
        } catch (Exception e) {
            return "Attribute table Seeded Failed";
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class AttributeType {
        private UUID id;
        private String name;
        private String unit_id;
    }

    public String CateSeeds() {
        try {
            List<CategoryType> cate = new ArrayList<>();
            cate.add(new CategoryType(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Category name 1"));
            cate.add(new CategoryType(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Category name 2"));
            cate.add(new CategoryType(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Category name 3"));

            for (CategoryType product : cate) {
                entityManager.createNativeQuery("INSERT INTO category (id, name, unit_id) VALUES (?, ?, ?)")
                        .setParameter(1, product.getId())
                        .setParameter(2, product.getName())
                        .executeUpdate();
            }
            return "Cate table Seeded";
        } catch (Exception e) {
            return "Cate table Seeded Failed";
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CategoryType {
        private UUID id;
        private String name;
    }

    public String unitSeeds() {
        try {
            List<UnitType> products = new ArrayList<>();
            products.add(new UnitType(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Unit name 1","String"));
            products.add(new UnitType(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Unit name 2", "String"));
            products.add(new UnitType(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Unit name 3", "String"));
            products.add(new UnitType(UUID.fromString("00000000-0000-0000-0000-000000000004"), "Unit name 4", "String"));
            products.add(new UnitType(UUID.fromString("00000000-0000-0000-0000-000000000005"), "Unit name 5", "String"));
            for (UnitType product : products) {
                entityManager.createNativeQuery("INSERT INTO unit (id, name, unit_type) VALUES (?, ?, ?)")
                        .setParameter(1, product.getId())
                        .setParameter(2, product.getName())
                        .setParameter(3, product.getUnit_type())
                        .executeUpdate();
            }
            return "Unit table Seeded";
        } catch (Exception e) {
            return "Unit table Seeded Failed";
        }
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class UnitType {
        private UUID id;
        private String name;
        private String unit_type;
    }
}