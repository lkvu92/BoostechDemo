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
import org.springframework.stereotype.Service;

import com.boostech.demo.entity.Attribute;
import com.boostech.demo.entity.PValue;
import com.boostech.demo.entity.Product;
import com.boostech.demo.exception.AttributeNotFoundException;
import com.boostech.demo.exception.PValueConflictException;
import com.boostech.demo.exception.PValueNotFoundException;
import com.boostech.demo.exception.ProductNotFoundException;
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

	@Override
	public FindPValueByIdResponse findById(UUID id, boolean includeDeleted) {
		Optional<PValue> pValueOptional = _pValueRepository.findByIdWithProductAndAttribute(id, includeDeleted);

		if (pValueOptional.isEmpty()) {
			throw new PValueNotFoundException(id);
		}

		PValue pValue =  pValueOptional.get();

		FindPValueByIdResponse response = new FindPValueByIdResponse();
		response.setId(id);
		response.setProductId(pValue.getProduct().getId());
		response.setAttributeId(pValue.getAttribute().getId());
		response.setValue(pValue.getValue());

		return response;
	}

	@Override
	public FindPValueByProductIdAndAttributeIdResponse findByProductIdAndAttributeId(
			DeleteValueByProductIdAndAttributeIdDto dto,
			boolean includeDeleted
	) {
		UUID productId = dto.getProductId(), attributeId = dto.getAttributeId();
		Optional<PValue> pValueOptional = _pValueRepository.findByProductIdAndAttributeId(productId, attributeId, includeDeleted);

		if (pValueOptional.isEmpty()) {
			throw new PValueNotFoundException(attributeId, productId);
		}

		PValue pValue = pValueOptional.get();

		FindPValueByProductIdAndAttributeIdResponse response = new FindPValueByProductIdAndAttributeIdResponse();
		response.setProductId(productId);
		response.setAttributeId(attributeId);
		response.setValue(pValue.getValue());

		return response;
	}

	@Override
	public List<FindPValuesByAttributeIdList> findAllByAttributeIdList(
			List<UUID> attributeIdList,
			boolean includeDeleted
	) {
		List<PValue> pValues = _pValueRepository.findAllByAttributeIdIn(attributeIdList, includeDeleted);

		List<FindPValuesByAttributeIdList> response = pValues.stream().map(pValue -> {
			FindPValuesByAttributeIdList findPValuesByAttributeIdList = new FindPValuesByAttributeIdList();
			findPValuesByAttributeIdList.setId(pValue.getId());
			findPValuesByAttributeIdList.setProductId(pValue.getProduct().getId());
			findPValuesByAttributeIdList.setValue(pValue.getValue());

			return findPValuesByAttributeIdList;
		}).toList();

		return response;
	}

	@Override
	public List<FindPValuesByProductIdList> findAllByProductIdList(
			List<UUID> productIdList,
			boolean includeDeleted
	) {
		List<PValue> pValues = _pValueRepository.findAllByProductIdIn(productIdList, includeDeleted);

		List<FindPValuesByProductIdList> response = pValues.stream().map(pValue -> {
			FindPValuesByProductIdList findPValuesByProductIdList = new FindPValuesByProductIdList();
			findPValuesByProductIdList.setId(pValue.getId());
			findPValuesByProductIdList.setAttributeId(pValue.getAttribute().getId());
			findPValuesByProductIdList.setValue(pValue.getValue());

			return findPValuesByProductIdList;
		}).toList();

		return response;
	}

	@Override
	public List<FindAllProductByCategoryIdAndAttributeIdAndValueResponse> findAllProductByCategoryIdAndAttributeIdAndValue(
			FindAllProductByCategoryIdAndAttributeIdValueUnitTuplesDto dto,
			boolean includeDeleted
	) {
		StringBuilder sqlStringBuilder = new StringBuilder("select p from Product p\r\n"
				+ "join Category c on c.id = p.category.id\r\n"
				+ "join PValue v on v.product.id = p.id\r\n"
				+ "join Attribute a on a.id = v.attribute.id\r\n"
				+ "where p.category.id = :categoryId\r\n");

		if (!includeDeleted) {
			sqlStringBuilder.append("and p.deletedAt is null\r\n");
		}

		List<AttributeIdValueUnitTuple> tuples = dto.getAttributeIdValueUnitTuples();

		int n = tuples.size();

		if (n > 0) {
			sqlStringBuilder.append("and (v.attribute.id, v.value) in (\r\n");
		}
		for (int i = 0; i < n; i++) {
			sqlStringBuilder.append(String.format("(:attributeId%s, :value%s)", i, i, i));
			if (i != n - 1) {
				sqlStringBuilder.append(",");
			}
			sqlStringBuilder.append("\r\n");
		}

		if (n > 0) {
			sqlStringBuilder.append(")\r\n");
		}

		sqlStringBuilder.append("group by p.id\r\n");

		if (n > 0) {
			sqlStringBuilder.append(String.format("having count(distinct (v.attribute.id, v.value)) = %d", n));
		}

		TypedQuery<Product> query = _entityManager.createQuery(sqlStringBuilder.toString(), Product.class);
		query.setParameter("categoryId", dto.getCategoryId());

		for (int i = 0; i < n; i++) {
			AttributeIdValueUnitTuple tuple = tuples.get(i);

			query.setParameter("attributeId" + i, tuple.id);
			query.setParameter("value" + i, tuple.value);
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
		UUID productId = dto.getProductId(), attributeId = dto.getAttributeId();

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

		Product product = productOptional.get();
		Attribute attribute = attributeOptional.get();

		PValue value = new PValue(product, attribute, dto.getValue());

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

		value.setValue(dto.getValue());

		_pValueRepository.save(value);
	}

	@Override
	public void updateValueByProductIdAndAttributeId(UpdateValueByProductIdAndAttributeIdDto dto) {
		UUID productId = dto.getProductId(), attributeId = dto.getAttributeId();
		Optional<PValue> valueOptional = _pValueRepository.findByProductIdAndAttributeId(productId,  attributeId, false);

		if (valueOptional.isEmpty()) {
			throw new PValueNotFoundException(attributeId, productId);
		}

		PValue value = valueOptional.get();

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
		Optional<PValue> valueOptional = _pValueRepository.findByProductIdAndAttributeId(productId, attributeId, true);

		if (valueOptional.isEmpty()) {
			throw new PValueNotFoundException(attributeId, productId);
		}

		PValue value = valueOptional.get();
		boolean delete = false;

		if (value.getDeletedAt() == null) {
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
			String value = tuple.getValue();
			Attribute attribute = tuple.getAttribute();
			PValue pValue = new PValue();

			pValue.setProduct(product);
			pValue.setAttribute(attribute);
			pValue.setValue(value);

			product.getValues().add(pValue);
		}

		_productRepository.save(product);
	}

}