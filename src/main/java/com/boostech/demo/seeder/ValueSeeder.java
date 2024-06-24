package com.boostech.demo.seeder;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValueSeeder {
	private EntityManager _entityManager;
	
	@Data
	private static class PValueSeeder {
		private UUID id;
		private String value;
		private UUID productId;
		private UUID attributeId;
		private UUID categoryId;
	}
	
	public void seeder() {
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
		
		StringBuilder queryString = new StringBuilder("insert into p_value (product_id, category_id, attribute_id, value) values (\r\n");
		for (int i = 0; i < n; i++) {
			queryString.append(String.format("(:productId%d, :categoryId%d, :attributeId%d, :value%d)\r\n", i, i, i, i));
		}
		queryString.append(")");
		
		Query query = _entityManager.createNativeQuery(queryString.toString());
		
		for (int i = 0; i < n; i++) {
			PValueSeeder pValue = pValues.get(i);
			query.setParameter(String.format(":productId%d", i), pValue.getProductId());
			query.setParameter(String.format(":attributeId%d", i), pValue.getAttributeId());
			query.setParameter(String.format(":categoryId%d", i), pValue.getCategoryId());
			query.setParameter(String.format(":value%d", i), pValue.getValue());
		}
		
		query.executeUpdate();
	}
}
