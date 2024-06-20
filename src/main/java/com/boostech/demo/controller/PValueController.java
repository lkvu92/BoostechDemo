package com.boostech.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.boostech.demo.dto.CreateValueByIdDto;
import com.boostech.demo.dto.FindAllProductByCategoryIdAndAttributeIdValuePairsDto;
import com.boostech.demo.entity.PValue;
import com.boostech.demo.entity.Product;
import com.boostech.demo.exception.ErrorInfo;
import com.boostech.demo.exception.PValueConflictException;
import com.boostech.demo.exception.PValueNotFoundException;
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
	public ResponseEntity<PValue> findById (
		@RequestParam(name="attribute_id", required = false) UUID attributeId, 
		@RequestParam(name="product_id", required = false) UUID productId
	) {
		PValue value = _pValueService.findById(attributeId, productId);
		
		return ResponseEntity.ok(value);
	}
	
	@GetMapping("attribute_id")
	public ResponseEntity<List<PValue>> findByAttributeIdList(@RequestBody List<UUID> attributeIdList) {
		List<PValue> values = _pValueService.findAllByAttributeIdList(attributeIdList);
		
		return ResponseEntity.ok(values);
	}
	
	@GetMapping("product_id")
	public ResponseEntity<List<PValue>> findByProductIdList(@RequestBody List<UUID> productIdList) {
		List<PValue> values = _pValueService.findAllByProductIdList(productIdList);
		
		return ResponseEntity.ok(values);
	}
	
	@GetMapping("products")
	public ResponseEntity<List<Product>> findAllProductByCategoryIdAndAttributeIdValuePairs(
			@RequestBody 
			FindAllProductByCategoryIdAndAttributeIdValuePairsDto request) {
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
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(PValueNotFoundException.class)
	@ResponseBody ErrorInfo handlePValueNotFound(HttpServletRequest req, Exception ex) {
	    return new ErrorInfo(HttpStatus.NOT_FOUND.value(), ex.getLocalizedMessage());
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(PValueConflictException.class)
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
