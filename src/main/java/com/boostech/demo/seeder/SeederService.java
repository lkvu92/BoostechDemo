package com.boostech.demo.seeder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
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
        valueSeeder();
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
            List<CategoryAttributeType> list_cate_atr = new ArrayList<>();
            list_cate_atr.add(new CategoryAttributeType(UUID.fromString("00000000-0000-0000-0000-000000000001"),UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"),true));
            list_cate_atr.add(new CategoryAttributeType(UUID.fromString("00000000-0000-0000-0000-000000000002"),UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000002"),true));
            list_cate_atr.add(new CategoryAttributeType(UUID.fromString("00000000-0000-0000-0000-000000000003"),UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000003"),true));
            list_cate_atr.add(new CategoryAttributeType(UUID.fromString("00000000-0000-0000-0000-000000000004"),UUID.fromString("00000000-0000-0000-0000-000000000002"), UUID.fromString("00000000-0000-0000-0000-000000000002"),true));
            list_cate_atr.add(new CategoryAttributeType(UUID.fromString("00000000-0000-0000-0000-000000000005"),UUID.fromString("00000000-0000-0000-0000-000000000002"), UUID.fromString("00000000-0000-0000-0000-000000000003"),true));
            list_cate_atr.add(new CategoryAttributeType(UUID.fromString("00000000-0000-0000-0000-000000000006"),UUID.fromString("00000000-0000-0000-0000-000000000003"), UUID.fromString("00000000-0000-0000-0000-000000000002"),true));
            list_cate_atr.add(new CategoryAttributeType(UUID.fromString("00000000-0000-0000-0000-000000000007"),UUID.fromString("00000000-0000-0000-0000-000000000003"), UUID.fromString("00000000-0000-0000-0000-000000000003"),true));
            list_cate_atr.add(new CategoryAttributeType(UUID.fromString("00000000-0000-0000-0000-000000000008"),UUID.fromString("00000000-0000-0000-0000-000000000003"), UUID.fromString("00000000-0000-0000-0000-000000000004"),false));
            list_cate_atr.add(new CategoryAttributeType(UUID.fromString("00000000-0000-0000-0000-000000000009"),UUID.fromString("00000000-0000-0000-0000-000000000003"), UUID.fromString("00000000-0000-0000-0000-000000000005"),false));
            for (CategoryAttributeType cate_atr : list_cate_atr) {
                entityManager.createNativeQuery("INSERT INTO category_attribute (id, category_id, attribute_id, is_required) VALUES (?, ?, ?, ?)")
                        .setParameter(1, cate_atr.getId())
                        .setParameter(2, cate_atr.getCategory_id())
                        .setParameter(3, cate_atr.getAttribute_id())
                        .setParameter(4, cate_atr.is_required())
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
            products.add(new AttributeType(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Attribute name 1", UUID.fromString("00000000-0000-0000-0000-000000000001")));
            products.add(new AttributeType(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Attribute name 2", UUID.fromString("00000000-0000-0000-0000-000000000002")));
            products.add(new AttributeType(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Attribute name 3", UUID.fromString("00000000-0000-0000-0000-000000000003")));
            products.add(new AttributeType(UUID.fromString("00000000-0000-0000-0000-000000000004"), "Attribute name 4", UUID.fromString("00000000-0000-0000-0000-000000000004")));
            products.add(new AttributeType(UUID.fromString("00000000-0000-0000-0000-000000000005"), "Attribute name 5", UUID.fromString("00000000-0000-0000-0000-000000000005")));
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
            List<CategoryType> cate = new ArrayList<>();
            cate.add(new CategoryType(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Category name 1"));
            cate.add(new CategoryType(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Category name 2"));
            cate.add(new CategoryType(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Category name 3"));

            for (CategoryType product : cate) {
                entityManager.createNativeQuery("INSERT INTO category (id, name) VALUES (?, ?)")
                        .setParameter(1, product.getId())
                        .setParameter(2, product.getName())
                        .executeUpdate();
            }
            return "Cate table Seeded";

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

    public void valueSeeder() {
        List<PValueSeeder> pValues = new ArrayList<>();
        String uuidSeeder = "00000000-0000-0000-0000-0000000000";

        int n = 16;

        for (int i = 0; i < n; i++) {
            PValueSeeder pValue = new PValueSeeder();
            pValue.setId(UUID.randomUUID());
            pValue.setValue("value " + i + 1);
            pValues.add(pValue);
        }

        PValueSeeder pValue1 = pValues.get(0);
        pValue1.setProductId(UUID.fromString(uuidSeeder + "01"));
        pValue1.setAttributeId(UUID.fromString(uuidSeeder + "01"));
        pValue1.setCategoryId(UUID.fromString(uuidSeeder + "01"));
        pValue1.setValue("10");

        PValueSeeder pValue2 = pValues.get(1);
        pValue2.setProductId(UUID.fromString(uuidSeeder + "01"));
        pValue2.setAttributeId(UUID.fromString(uuidSeeder + "02"));
        pValue2.setCategoryId(UUID.fromString(uuidSeeder + "01"));
        pValue2.setValue("50");

        PValueSeeder pValue3 = pValues.get(2);
        pValue3.setProductId(UUID.fromString(uuidSeeder + "01"));
        pValue3.setAttributeId(UUID.fromString(uuidSeeder + "03"));
        pValue3.setCategoryId(UUID.fromString(uuidSeeder + "01"));

        PValueSeeder pValue4 = pValues.get(3);
        pValue4.setProductId(UUID.fromString(uuidSeeder + "02"));
        pValue4.setAttributeId(UUID.fromString(uuidSeeder + "01"));
        pValue4.setCategoryId(UUID.fromString(uuidSeeder + "01"));
        pValue4.setValue("10");

        PValueSeeder pValue5 = pValues.get(4);
        pValue5.setProductId(UUID.fromString(uuidSeeder + "02"));
        pValue5.setAttributeId(UUID.fromString(uuidSeeder + "02"));
        pValue5.setCategoryId(UUID.fromString(uuidSeeder + "01"));
        pValue5.setValue("50");

        PValueSeeder pValue6 = pValues.get(5);
        pValue6.setProductId(UUID.fromString(uuidSeeder + "02"));
        pValue6.setAttributeId(UUID.fromString(uuidSeeder + "03"));
        pValue6.setCategoryId(UUID.fromString(uuidSeeder + "01"));

        PValueSeeder pValue7 = pValues.get(6);
        pValue7.setProductId(UUID.fromString(uuidSeeder + "03"));
        pValue7.setAttributeId(UUID.fromString(uuidSeeder + "02"));
        pValue7.setCategoryId(UUID.fromString(uuidSeeder + "02"));
        pValue7.setValue("50");

        PValueSeeder pValue8 = pValues.get(7);
        pValue8.setProductId(UUID.fromString(uuidSeeder + "03"));
        pValue8.setAttributeId(UUID.fromString(uuidSeeder + "02"));
        pValue8.setCategoryId(UUID.fromString(uuidSeeder + "03"));

        PValueSeeder pValue9 = pValues.get(8);
        pValue9.setProductId(UUID.fromString(uuidSeeder + "04"));
        pValue9.setAttributeId(UUID.fromString(uuidSeeder + "02"));
        pValue9.setCategoryId(UUID.fromString(uuidSeeder + "03"));

        PValueSeeder pValue10 = pValues.get(9);
        pValue10.setProductId(UUID.fromString(uuidSeeder + "04"));
        pValue10.setAttributeId(UUID.fromString(uuidSeeder + "04"));
        pValue10.setCategoryId(UUID.fromString(uuidSeeder + "03"));

        PValueSeeder pValue11 = pValues.get(10);
        pValue11.setProductId(UUID.fromString(uuidSeeder + "04"));
        pValue11.setAttributeId(UUID.fromString(uuidSeeder + "05"));
        pValue11.setCategoryId(UUID.fromString(uuidSeeder + "03"));

        PValueSeeder pValue12 = pValues.get(11);
        pValue12.setProductId(UUID.fromString(uuidSeeder + "04"));
        pValue12.setAttributeId(UUID.fromString(uuidSeeder + "03"));
        pValue12.setCategoryId(UUID.fromString(uuidSeeder + "03"));

        PValueSeeder pValue13 = pValues.get(12);
        pValue13.setProductId(UUID.fromString(uuidSeeder + "05"));
        pValue13.setAttributeId(UUID.fromString(uuidSeeder + "02"));
        pValue13.setCategoryId(UUID.fromString(uuidSeeder + "03"));

        PValueSeeder pValue14 = pValues.get(13);
        pValue14.setProductId(UUID.fromString(uuidSeeder + "05"));
        pValue14.setAttributeId(UUID.fromString(uuidSeeder + "03"));
        pValue14.setCategoryId(UUID.fromString(uuidSeeder + "03"));

        PValueSeeder pValue15 = pValues.get(14);
        pValue15.setProductId(UUID.fromString(uuidSeeder + "05"));
        pValue15.setAttributeId(UUID.fromString(uuidSeeder + "04"));
        pValue15.setCategoryId(UUID.fromString(uuidSeeder + "03"));

        PValueSeeder pValue16 = pValues.get(15);
        pValue16.setProductId(UUID.fromString(uuidSeeder + "05"));
        pValue16.setAttributeId(UUID.fromString(uuidSeeder + "05"));
        pValue16.setCategoryId(UUID.fromString(uuidSeeder + "03"));
        pValue16.setValue("10");

        StringBuilder queryString = new StringBuilder("insert into p_value (id, product_id, category_id, attribute_id, value) values\r\n");
        for (int i = 0; i < n - 1; i++) {
            queryString.append(String.format("(:id%d, :productId%d, :categoryId%d, :attributeId%d, :value%d),\r\n", i, i, i, i, i));
        }
        queryString.append(String.format("(:id%d, :productId%d, :categoryId%d, :attributeId%d, :value%d)", n - 1, n - 1, n - 1, n - 1, n - 1));

        Query query = entityManager.createNativeQuery(queryString.toString());

        for (int i = 0; i < n; i++) {
            PValueSeeder pValue = pValues.get(i);
            query.setParameter(String.format("id%d", i), pValue.getId());
            query.setParameter(String.format("productId%d", i), pValue.getProductId());
            query.setParameter(String.format("attributeId%d", i), pValue.getAttributeId());
            query.setParameter(String.format("categoryId%d", i), pValue.getCategoryId());
            query.setParameter(String.format("value%d", i), pValue.getValue());
        }

        query.executeUpdate();
    }

    @Data
    private static class PValueSeeder {
        private UUID id;
        private String value;
        private UUID productId;
        private UUID attributeId;
        private UUID categoryId;
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
        private UUID unit_id;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CategoryType {
        private UUID id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CategoryAttributeType {
        private UUID id;
        private UUID category_id;
        private UUID attribute_id;
        boolean is_required;
    }
}
