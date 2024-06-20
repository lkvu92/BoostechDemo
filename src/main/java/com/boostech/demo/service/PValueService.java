package com.boostech.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.boostech.demo.dto.FindAllProductByCategoryIdAndAttributeIdValuePairsDto;
import com.boostech.demo.dto.AttributeIdValuePair;
import com.boostech.demo.entity.Attribute;
import com.boostech.demo.entity.PValue;
import com.boostech.demo.entity.PValuePrimaryKey;
import com.boostech.demo.entity.Product;
import com.boostech.demo.exception.PValueNotFoundException;
import com.boostech.demo.repository.PValueRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PValueService implements IPValueService {
	private final PValueRepository _pValueRepository;
	private final EntityManager _entityManager;
	
	@Override
	public PValue findById(UUID attributeId, UUID productId) {
		Attribute attribute = new Attribute();
		attribute.setId(attributeId);
		
		Product product = new Product();
		product.setId(productId);
		
		PValuePrimaryKey valueId = new PValuePrimaryKey();
		valueId.setAttribute(attribute);
		valueId.setProduct(product);
		
		PValue pValue = new PValue();
		pValue.setValueId(valueId);
		
		Optional<PValue> pValueOptional = _pValueRepository.findOne(Example.of(pValue));
		
		if (pValueOptional.isEmpty()) {
			throw new PValueNotFoundException(attributeId, productId);
		}
		
		return pValueOptional.get();
	}

	@Override
	public List<PValue> findAllByAttributeIdList(List<UUID> attributeIdList) {
		List<PValue> values = _pValueRepository.findAllByValueId_AttributeIdIn(attributeIdList);
		
		return values;
	}

	@Override
	public List<PValue> findAllByProductIdList(List<UUID> productIdList) {
		List<PValue> values = _pValueRepository.findAllByValueId_ProductIdIn(productIdList);
	
		return values;
	}

	@Override
	public List<Product> findAllProductByCategoryIdAndAttributeIdAndValue(FindAllProductByCategoryIdAndAttributeIdValuePairsDto dto) {
		StringBuilder sqlStringBuilder = new StringBuilder("select p.id, p.name from Product p\r\n"
				+ "join Category c on c.id = p.category.id\r\n"
				+ "join PValue v on  v.valueId.product.id = p.id\r\n"
				+ "join Attribute a on a.id = v.valueId.attribute.id\r\n"
				+ "where p.category.id = :categoryId\r\n");
		
		List<AttributeIdValuePair> pairs = dto.getAttributeIdValuePairs();
		
	    for (int i = 0; i < pairs.size(); i++) {
		    sqlStringBuilder.append(String.format("and v.valueId.attribute.id = :attributeId%s and v.value = :value%s\r\n", i, i));
	    }
	    
	    TypedQuery<Product> query = _entityManager.createQuery(sqlStringBuilder.toString(), Product.class);
	    query.setParameter("categoryId", dto.getCategoryId());
	    
	    for (int i = 0; i < pairs.size(); i++) {
	    	query.setParameter("attributeId" + i, pairs.get(i).id);
	    	query.setParameter("value" + i, pairs.get(i).value);
	    }
	    
	    
	   return  query.getResultList();
	}

}
