package com.boostech.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.boostech.demo.dto.FindAllProductByCategoryIdAndAttributeIdValuePairsDto;
import com.boostech.demo.dto.AttributeIdValuePair;
import com.boostech.demo.dto.CreateValueByIdDto;
import com.boostech.demo.dto.DeleteValueByIdDto;
import com.boostech.demo.entity.Attribute;
import com.boostech.demo.entity.PValue;
import com.boostech.demo.entity.PValuePrimaryKey;
import com.boostech.demo.entity.Product;
import com.boostech.demo.exception.AttributeNotFoundException;
import com.boostech.demo.exception.PValueConflictException;
import com.boostech.demo.exception.PValueNotFoundException;
import com.boostech.demo.exception.ProductNotFoundException;
import com.boostech.demo.repository.IAttributeRepository;
import com.boostech.demo.repository.PValueRepository;
import com.boostech.demo.repository.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PValueService implements IPValueService {
	private final PValueRepository _pValueRepository;
	private final IAttributeRepository _attributeRepository;
	private final ProductRepository _productRepository;
	private final EntityManager _entityManager;
	
	@Override
	public PValue findById(DeleteValueByIdDto dto) {
		Attribute attribute = new Attribute();
		attribute.setId(dto.getAttributeId());
		
		Product product = new Product();
		product.setId(dto.getProductId());
		
		PValuePrimaryKey valueId = new PValuePrimaryKey();
		valueId.setAttribute(attribute);
		valueId.setProduct(product);
		
		PValue pValue = new PValue();
		pValue.setValueId(valueId);
		
		Optional<PValue> pValueOptional = _pValueRepository.findOne(Example.of(pValue));
		
		if (pValueOptional.isEmpty()) {
			throw new PValueNotFoundException(dto.getAttributeId(), dto.getProductId());
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

	@Override
	@Transactional
	public void createValueById(CreateValueByIdDto dto) {
		String checkExistQueryString = "select v from PValue v where v.valueId.product.id = :productId and v.valueId.attribute.id = :attributeId";
		
		TypedQuery<PValue> checkExistQuery = _entityManager.createQuery(checkExistQueryString, PValue.class);
		
		checkExistQuery.setParameter("productId", dto.getProductId());
	    checkExistQuery.setParameter("attributeId", dto.getAttributeId());
		
		List<PValue> values =  checkExistQuery.getResultList();
		
		if (values.size() > 0) {
			throw new PValueConflictException(String.format("Value existed on attribute id: '%s' and product id: '%s'", dto.getAttributeId().toString(), dto.getProductId().toString()));
		}
		
		if (!_productRepository.existsById(dto.getProductId())) {
			throw new ProductNotFoundException(dto.getProductId());
		}
		
		if (!_attributeRepository.existsById(dto.getAttributeId())) {
			throw new AttributeNotFoundException(dto.getAttributeId());
		}
		
		String createQueryString = "INSERT INTO p_value (attribute_id, product_id, value)"
	             + " VALUES (:attributeId, :productId, :value)";

		Query createQuery = _entityManager.createNativeQuery(createQueryString)
		    .setParameter("productId", dto.getProductId())
		    .setParameter("attributeId", dto.getAttributeId())
		    .setParameter("value", dto.getValue());
		
		createQuery.executeUpdate();
	}

	@Override
	public void updateValueById(CreateValueByIdDto dto) {
		DeleteValueByIdDto keys = new DeleteValueByIdDto(dto.getProductId(), dto.getAttributeId());
		PValue value = findById(keys);
		
		value.setValue(dto.getValue());
		_pValueRepository.save(value);
	}

	@Override
	public void deleteValueById(DeleteValueByIdDto dto) {
		PValue value = findById(dto);
		
		value.setDeletedAt(LocalDateTime.now());
		_pValueRepository.save(value);
	}
	
}
