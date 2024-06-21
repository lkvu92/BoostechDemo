package com.boostech.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.boostech.demo.dto.CreateValueByIdDto;
import com.boostech.demo.dto.DeleteValueByProductIdAndAttributeIdDto;
import com.boostech.demo.dto.FindAllProductByCategoryIdAndAttributeIdValueUnitTuplesDto;
import com.boostech.demo.dto.UpdateValueByIdDto;
import com.boostech.demo.entity.PValue;
import com.boostech.demo.entity.Product;
import com.boostech.demo.exception.AttributeNotFoundException;
import com.boostech.demo.exception.ErrorInfo;
import com.boostech.demo.exception.PValueConflictException;
import com.boostech.demo.exception.PValueNotFoundException;
import com.boostech.demo.exception.ProductNotFoundException;
import com.boostech.demo.exception.UnitNotFoundException;
import com.boostech.demo.exception.UnitNotInAttributeException;
import com.boostech.demo.service.IPValueService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/pvalues")
@RequiredArgsConstructor
@ControllerAdvice
public class PValueController {
	private final IPValueService _pValueService;
	
	@GetMapping
	public ResponseEntity<PValue> findByProductIdAndAttributeId( 
			@RequestBody DeleteValueByProductIdAndAttributeIdDto request,
			@RequestParam(name = "includeDeleted") boolean includeDeleted) {
		PValue value = _pValueService.findByProductIdAndAttributeId(request);
		
		return ResponseEntity.ok(value);
	}

	@GetMapping("{id}")
	public ResponseEntity<PValue> findById(
			@PathVariable UUID id,
			@RequestParam(name = "includeDeleted") boolean includeDeleted) {
		PValue value = _pValueService.findById(id);

		return ResponseEntity.ok(value);
	}
	
	@GetMapping("attribute_id")
	public ResponseEntity<List<PValue>> findByAttributeIdList(
			@RequestBody List<UUID> attributeIdList,
			@RequestParam(name = "includeDeleted") boolean includeDeleted) {
		List<PValue> values = _pValueService.findAllByAttributeIdList(attributeIdList);
		
		return ResponseEntity.ok(values);
	}
	
	@GetMapping("product_id")
	public ResponseEntity<List<PValue>> findByProductIdList(
			@RequestBody List<UUID> productIdList,
			@RequestParam(name = "includeDeleted") boolean includeDeleted) {
		List<PValue> values = _pValueService.findAllByProductIdList(productIdList);
		
		return ResponseEntity.ok(values);
	}
	
	@GetMapping("products")
	public ResponseEntity<List<Product>> findAllProductByCategoryIdAndAttributeIdValuePairs(
			@RequestBody FindAllProductByCategoryIdAndAttributeIdValueUnitTuplesDto request,
			@RequestParam(name = "includeDeleted") boolean includeDeleted) {
		List<Product> products = _pValueService.findAllProductByCategoryIdAndAttributeIdAndValue(request);
		
		return ResponseEntity.ok(products);
		
	}
	
	@PostMapping
	public ResponseEntity<?> createValueById(
			@RequestBody 
			CreateValueByIdDto request) {
		_pValueService.createValueById(request);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	@PatchMapping("{id}")
	public ResponseEntity<?> updateValueById(
			@PathVariable UUID id,
			@RequestBody 
			UpdateValueByIdDto request) {
		
		request.setId(id);
		_pValueService.updateValueById(request);
		
		return ResponseEntity.ok().build();
		
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteValueById(
			@PathVariable UUID id) {
		boolean delete = _pValueService.deleteValueById(id);
		
		return ResponseEntity.ok().build();
		
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteValueById(
			@RequestBody DeleteValueByProductIdAndAttributeIdDto request) {
		boolean delete = _pValueService.deleteValueByProductIdAndAttributeId(request);
		
		return ResponseEntity.ok().build();
		
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({PValueNotFoundException.class, ProductNotFoundException.class, AttributeNotFoundException.class, UnitNotFoundException.class})
	@ResponseBody ErrorInfo handlePValueNotFound(HttpServletRequest req, Exception ex) {
	    return new ErrorInfo(HttpStatus.NOT_FOUND.value(), ex.getLocalizedMessage());
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler({PValueConflictException.class, UnitNotInAttributeException.class})
	@ResponseBody ErrorInfo handlePValueConflict(HttpServletRequest req, Exception ex) {
	    return new ErrorInfo(HttpStatus.CONFLICT.value(), ex.getLocalizedMessage());
	}
	
	@ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(Exception ex) {
	    if (ex instanceof IllegalArgumentException && ex.getMessage().contains("UUID") ||
	    		ex instanceof HttpMessageNotReadableException && ex.getMessage().contains("JSON parse")) {
	        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), "Invalid UUID format");
	        return ResponseEntity.badRequest().body(errorInfo);
	    }
	    
	    return ResponseEntity.internalServerError().build();
    }
	
}
