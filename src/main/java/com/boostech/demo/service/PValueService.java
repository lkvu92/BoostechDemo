package com.boostech.demo.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.boostech.demo.dto.*;
import com.boostech.demo.dto.resDto.FindAllProductByCategoryIdAndAttributeIdAndValueResponse;
import com.boostech.demo.dto.resDto.FindPValueByIdResponse;
import com.boostech.demo.dto.resDto.FindPValueByProductIdAndAttributeIdResponse;
import com.boostech.demo.dto.resDto.FindPValuesByAttributeIdList;
import com.boostech.demo.dto.resDto.FindPValuesByProductIdList;
import com.boostech.demo.entity.Unit;
import com.boostech.demo.repository.IUnitRepository;
import org.springframework.stereotype.Service;

import com.boostech.demo.entity.Attribute;
import com.boostech.demo.entity.PValue;
import com.boostech.demo.entity.Product;
import com.boostech.demo.exception.AttributeNotFoundException;
import com.boostech.demo.exception.PValueConflictException;
import com.boostech.demo.exception.PValueNotFoundException;
import com.boostech.demo.exception.ProductNotFoundException;
import com.boostech.demo.exception.UnitNotFoundException;
import com.boostech.demo.exception.UnitNotInAttributeException;
import com.boostech.demo.repository.IAttributeRepository;
import com.boostech.demo.repository.PValueRepository;
import com.boostech.demo.repository.ProductRepository;

import jakarta.persistence.EntityManager;
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
	private final IUnitRepository _unitRepository;

	@Override
	public FindPValueByIdResponse findById(UUID id) {
		Optional<PValue> pValueOptional = _pValueRepository.findById(id);

		if (pValueOptional.isEmpty()) {
			throw new PValueNotFoundException(id);
		}

		PValue pValue =  pValueOptional.get();
		
		FindPValueByIdResponse response = new FindPValueByIdResponse();
		response.setId(id);
		response.setProductId(pValue.getProduct().getId());
		response.setAttributeId(pValue.getAttribute().getId());
		response.setType(pValue.getUnit().getUnitType());
		response.setValue(pValue.getValue());
		response.setUnitName(pValue.getUnit().getUnitName());
		
		return response;
	}

	@Override
	public FindPValueByProductIdAndAttributeIdResponse findByProductIdAndAttributeId(DeleteValueByProductIdAndAttributeIdDto dto) {
		UUID productId = dto.getProductId(), attributeId = dto.getAttributeId();
		
		Optional<PValue> pValueOptional = _pValueRepository.findByProductIdAndAttributeId(productId, attributeId);
		if (pValueOptional.isEmpty()) {
			throw new PValueNotFoundException(attributeId, productId);
		}
		
		PValue pValue = pValueOptional.get();
		
		FindPValueByProductIdAndAttributeIdResponse response = new FindPValueByProductIdAndAttributeIdResponse();
		response.setProductId(productId);
		response.setAttributeId(attributeId);
		response.setType(pValue.getUnit().getUnitType());
		response.setValue(pValue.getValue());
		response.setUnitName(pValue.getUnit().getUnitName());
		
		return response;
	}

	@Override
	public List<FindPValuesByAttributeIdList> findAllByAttributeIdList(List<UUID> attributeIdList) {
		List<PValue> pValues = _pValueRepository.findAllByAttributeIdIn(attributeIdList);
		
		List<FindPValuesByAttributeIdList> response = pValues.stream().map(pValue -> {
			FindPValuesByAttributeIdList findPValuesByAttributeIdList = new FindPValuesByAttributeIdList();
			findPValuesByAttributeIdList.setId(pValue.getId());
			findPValuesByAttributeIdList.setProductId(pValue.getProduct().getId());
			findPValuesByAttributeIdList.setValue(pValue.getValue());
			findPValuesByAttributeIdList.setUnitName(pValue.getUnit().getUnitName());
			findPValuesByAttributeIdList.setType(pValue.getUnit().getUnitType());
			
			return findPValuesByAttributeIdList;
		}).toList();
		
		return response;
	}

	@Override
	public List<FindPValuesByProductIdList> findAllByProductIdList(List<UUID> productIdList) {
		List<PValue> pValues = _pValueRepository.findAllByProductIdIn(productIdList);
		
		List<FindPValuesByProductIdList> response = pValues.stream().map(pValue -> {
			FindPValuesByProductIdList findPValuesByProductIdList = new FindPValuesByProductIdList();
			findPValuesByProductIdList.setId(pValue.getId());
			findPValuesByProductIdList.setAttributeId(pValue.getAttribute().getId());
			findPValuesByProductIdList.setValue(pValue.getValue());
			findPValuesByProductIdList.setUnitName(pValue.getUnit().getUnitName());
			findPValuesByProductIdList.setType(pValue.getUnit().getUnitType());
			
			return findPValuesByProductIdList;
		}).toList();
	
		return response;
	}

	@Override
	public List<FindAllProductByCategoryIdAndAttributeIdAndValueResponse> findAllProductByCategoryIdAndAttributeIdAndValue(FindAllProductByCategoryIdAndAttributeIdValueUnitTuplesDto dto) {
		StringBuilder sqlStringBuilder = new StringBuilder("select p from Product p\r\n"
				+ "join Category c on c.id = p.category.id\r\n"
				+ "join PValue v on v.product.id = p.id\r\n"
				+ "join Attribute a on a.id = v.attribute.id\r\n"
				+ "where p.category.id = :categoryId\r\n");
		
		List<AttributeIdValueUnitTuple> tuples = dto.getAttributeIdValueUnitTuples();
		
	    for (int i = 0; i < tuples.size(); i++) {
		    sqlStringBuilder.append(String.format("and v.attribute.id = :attributeId%s\r\n"
		    		+ "and v.value = :value%s\r\n"
		    		+ "and v.unit.id = :unitId%s\r\n", i, i));
	    }
	    
	    TypedQuery<Product> query = _entityManager.createQuery(sqlStringBuilder.toString(), Product.class);
	    query.setParameter("categoryId", dto.getCategoryId());
	    
	    for (int i = 0; i < tuples.size(); i++) {
	    	AttributeIdValueUnitTuple tuple = tuples.get(i);
	    	
	    	query.setParameter("attributeId" + i, tuple.id);
	    	query.setParameter("value" + i, tuple.value);
	    	query.setParameter("unitId" + i, tuple.unitId);
	    }
	    
	    
	   List<Product> products = query.getResultList();
	   
	   List<FindAllProductByCategoryIdAndAttributeIdAndValueResponse> response = products.stream().map(product -> {
		   FindAllProductByCategoryIdAndAttributeIdAndValueResponse findAllProductByCategoryIdAndAttributeIdAndValueResponse = new FindAllProductByCategoryIdAndAttributeIdAndValueResponse();
		   findAllProductByCategoryIdAndAttributeIdAndValueResponse.setId(product.getId());
		   findAllProductByCategoryIdAndAttributeIdAndValueResponse.setName(product.getName());
			
			return findAllProductByCategoryIdAndAttributeIdAndValueResponse;
		}).toList();
	   
	   return response;
	}

	@Override
	@Transactional
	public void createValueById(CreateValueByIdDto dto) {
		UUID productId = dto.getProductId(), attributeId = dto.getAttributeId(), unitId = dto.getUnitId();
		
		if (_pValueRepository.existsByProductIdAndAttributeId(productId, attributeId)) {
			throw new PValueConflictException(String.format("value existed on 'product id' = %s and 'attribute id' = %s", productId, attributeId));
		}
		
		Optional<Product> productOptional = _productRepository.findById(productId);
		if (productOptional.isEmpty()) {
			throw new ProductNotFoundException(productId);
		}
		
		Optional<Attribute> attributeOptional = _attributeRepository.findById(attributeId);
		if (attributeOptional.isEmpty()) {
			throw new AttributeNotFoundException(attributeId);
		}

		Optional<Unit> unitOptional = _unitRepository.findById(unitId);
		if (unitOptional.isEmpty()) {
			throw new UnitNotFoundException(unitId);
		}
		
		boolean checkUnitInAttribute = _attributeRepository.existsByIdAndUnitsId(attributeId, unitId);
		if (!checkUnitInAttribute) {
			throw new UnitNotInAttributeException(String.format("Unit '%s' not in attribute '%s'", unitId, attributeId)); 
		}

		Product product = productOptional.get();
		Attribute attribute = attributeOptional.get();
		Unit unit = unitOptional.get();
		
		PValue value = new PValue(product, attribute, dto.getValue(), unit);
		
		_pValueRepository.save(value);
	}

	@Override
	public void updateValueById(UpdateValueByIdDto dto) {
		UUID id = dto.getId();
		Optional<PValue> valueOptional = _pValueRepository.findById(id);
		
		if (valueOptional.isEmpty()) {
			throw new PValueNotFoundException(id);
		}
		
		PValue value = valueOptional.get();
		UUID productId = dto.getProductId(), attributeId = dto.getAttributeId();
		boolean change = false;
		
		if (productId != null) {
			Optional<Product> productOptional = _productRepository.findById(productId);
			if (productOptional.isEmpty()) {
				throw new ProductNotFoundException(productId);
			}
			
			value.setProduct(productOptional.get());
			change = true;
		}
		
		if (attributeId != null) {
			Optional<Attribute> attributeOptional = _attributeRepository.findById(attributeId);
			if (attributeOptional.isEmpty()) {
				throw new AttributeNotFoundException(attributeId);
			}
			
			value.setAttribute(attributeOptional.get());
			change = true;
		}
		
		if (change && _pValueRepository.existsByProductIdAndAttributeId(productId, attributeId)) {
			throw new PValueConflictException(String.format("value existed on 'product id' = %s and 'attribute id' = %s", productId, attributeId));
		}
		
		UUID unitId = dto.getUnitId();
		if (unitId != null) {
			Optional<Unit> unitOptional = _unitRepository.findById(unitId);
			if (unitOptional.isEmpty()) {
				throw new UnitNotFoundException(unitId);
			}
			
			boolean checkUnitInAttribute = _attributeRepository.existsByIdAndUnitsId(attributeId, unitId);
			if (!checkUnitInAttribute) {
				throw new UnitNotInAttributeException(String.format("Unit '%s' not in attribute '%s'", unitId, attributeId)); 
			}
			
			value.setUnit(unitOptional.get());
		}
		
		value.setValue(dto.getValue());
		
		_pValueRepository.save(value);
	}
	
	@Override
	public void updateValueByProductIdAndAttributeId(UpdateValueByProductIdAndAttributeIdDto dto) {
		UUID productId = dto.getProductId(), attributeId = dto.getAttributeId();
		Optional<PValue> valueOptional = _pValueRepository.findByProductIdAndAttributeId(productId,  attributeId);
		
		if (valueOptional.isEmpty()) {
			throw new PValueNotFoundException(attributeId, productId);
		}
		
		PValue value = valueOptional.get();
		
		UUID unitId = dto.getUnitId();
		if (unitId != null) {
			Optional<Unit> unitOptional = _unitRepository.findById(unitId);
			if (unitOptional.isEmpty()) {
				throw new UnitNotFoundException(unitId);
			}
			
			boolean checkUnitInAttribute = _attributeRepository.existsByIdAndUnitsId(attributeId, unitId);
			if (!checkUnitInAttribute) {
				throw new UnitNotInAttributeException(String.format("Unit '%s' not in attribute '%s'", unitId, attributeId)); 
			}
			
			value.setUnit(unitOptional.get());
		}
		
		value.setValue(dto.getValue());
		
		_pValueRepository.save(value);
		
	}

	@Override
	public boolean deleteValueById(UUID id) {
		Optional<PValue> valueOptional = _pValueRepository.findById(id);
		
		if (valueOptional.isEmpty()) {
			throw new PValueNotFoundException(id);
		}
		
		PValue value = valueOptional.get();
		boolean delete = false;
		
		if (value.getDeletedAt() != null) {
			value.setDeletedAt(LocalDateTime.now());
			delete = true;
		}
		else {
			value.setDeletedAt(null);
		}
		
		_pValueRepository.save(value);
		
		return delete;
	}
	
	@Override
	public boolean deleteValueByProductIdAndAttributeId(DeleteValueByProductIdAndAttributeIdDto dto) {
		UUID productId = dto.getProductId(), attributeId = dto.getAttributeId();
		Optional<PValue> valueOptional = _pValueRepository.findByProductIdAndAttributeId(productId, attributeId);
		
		if (valueOptional.isEmpty()) {
			throw new PValueNotFoundException(attributeId, productId);
		}
		
		PValue value = valueOptional.get();
		boolean delete = false;
		
		if (value.getDeletedAt() != null) {
			value.setDeletedAt(LocalDateTime.now());
			delete = true;
		}
		else {
			value.setDeletedAt(null);
		}
		
		_pValueRepository.save(value);
		
		return delete;
	}

	@Override
	public void createValueByProductIdAndAttributeIdValueUnitTuples(Product product, List<AttributeValueUnitTuple> attributeIdValueUnitTuples) {
		for (AttributeValueUnitTuple tuple : attributeIdValueUnitTuples) {
			UUID unitId = tuple.getUnitId();
			String value = tuple.getValue();
			Attribute attribute = tuple.getAttribute();
			PValue pValue = new PValue();

			if (unitId != null) {
				Optional<Unit> unitOptional = _unitRepository.findById(unitId);
				if (unitOptional.isEmpty()) {
					throw new UnitNotFoundException(unitId);
				}

				boolean checkUnitInAttribute = _attributeRepository.existsByIdAndUnitsId(attribute.getId(), unitId);
				if (!checkUnitInAttribute) {
					throw new UnitNotInAttributeException(String.format("Unit '%s' not in attribute '%s'", unitId, attribute.getId()));
				}

				pValue.setUnit(unitOptional.get());
			}

			pValue.setProduct(product);
			pValue.setAttribute(attribute);
			pValue.setValue(value);

			product.getValues().add(pValue);
		}

		_productRepository.save(product);
	}

}
