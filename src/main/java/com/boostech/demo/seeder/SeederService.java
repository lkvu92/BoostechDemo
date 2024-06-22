package com.boostech.demo.seeder;

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
public class SeederService {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public String run() {
        unitSeeds();
        attributeSeeds();
        CateSeeds();
        CategoryAttributeSeeder();
        ProductDataSeeder();
        //pvarSeeds();
        return "Seeded run successfully";
    }

    public String ProductDataSeeder() {
        try {
            List<ProductType> products = new ArrayList<>();
            products.add(new ProductType(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Product 1", UUID.fromString("00000000-0000-0000-0000-000000000001")));
            products.add(new ProductType(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Product 2", UUID.fromString("00000000-0000-0000-0000-000000000001")));
            products.add(new ProductType(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Product 3", UUID.fromString("00000000-0000-0000-0000-000000000002")));
            products.add(new ProductType(UUID.fromString("00000000-0000-0000-0000-000000000004"), "Product 4", UUID.fromString("00000000-0000-0000-0000-000000000003")));
            products.add(new ProductType(UUID.fromString("00000000-0000-0000-0000-000000000005"), "Product 5", UUID.fromString("00000000-0000-0000-0000-000000000003")));
            for (ProductType product : products) {
                entityManager.createNativeQuery("INSERT INTO product (id, name,cate_id) VALUES (?, ?, ?)")
                        .setParameter(1, product.getId())
                        .setParameter(2, product.getName())
                        .setParameter(3, product.getCate_id())
                        .executeUpdate();
            }
            return "Product data Seeded run successfully";
        } catch (Exception e) {
            return "Product data Seeded Failed";
        }
    }

    public String CategoryAttributeSeeder() {
        try {
            List<Cat_Attr> list_cate_atr = new ArrayList<>();
            list_cate_atr.add(new Cat_Attr(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001")));
            list_cate_atr.add(new Cat_Attr(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000002")));
            list_cate_atr.add(new Cat_Attr(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000003")));
            list_cate_atr.add(new Cat_Attr(UUID.fromString("00000000-0000-0000-0000-000000000002"), UUID.fromString("00000000-0000-0000-0000-000000000002")));
            list_cate_atr.add(new Cat_Attr(UUID.fromString("00000000-0000-0000-0000-000000000002"), UUID.fromString("00000000-0000-0000-0000-000000000003")));
            list_cate_atr.add(new Cat_Attr(UUID.fromString("00000000-0000-0000-0000-000000000003"), UUID.fromString("00000000-0000-0000-0000-000000000002")));
            list_cate_atr.add(new Cat_Attr(UUID.fromString("00000000-0000-0000-0000-000000000003"), UUID.fromString("00000000-0000-0000-0000-000000000003")));
            list_cate_atr.add(new Cat_Attr(UUID.fromString("00000000-0000-0000-0000-000000000003"), UUID.fromString("00000000-0000-0000-0000-000000000004")));
            list_cate_atr.add(new Cat_Attr(UUID.fromString("00000000-0000-0000-0000-000000000003"), UUID.fromString("00000000-0000-0000-0000-000000000005")));
            for (Cat_Attr cate_atr : list_cate_atr) {
                entityManager.createNativeQuery("INSERT INTO category_attribute (category_id, attribute_id) VALUES (?, ?)")
                        .setParameter(1, cate_atr.getCategory_id())
                        .setParameter(2, cate_atr.getAttribute_id())
                        .executeUpdate();
            }
            return "Category_Attribute data Seeded run successfully";
        } catch (Exception e) {
            return "Category_Attribute data Seeded Failed";
        }
    }

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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ProductType {
        private UUID id;
        private String name;
        private UUID cate_id;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Cat_Attr {
        private UUID category_id;
        private UUID attribute_id;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class AttributeType {
        private UUID id;
        private String name;
        private String unit_id;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CategoryType {
        private UUID id;
        private String name;
    }
}
