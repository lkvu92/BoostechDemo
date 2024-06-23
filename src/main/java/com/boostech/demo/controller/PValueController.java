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
import com.boostech.demo.dto.resDto.FindAllProductByCategoryIdAndAttributeIdAndValueResponse;
import com.boostech.demo.dto.resDto.FindPValueByIdResponse;
import com.boostech.demo.dto.resDto.FindPValueByProductIdAndAttributeIdResponse;
import com.boostech.demo.dto.resDto.FindPValuesByAttributeIdList;
import com.boostech.demo.dto.resDto.FindPValuesByProductIdList;
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
	public ResponseEntity<FindPValueByProductIdAndAttributeIdResponse> findByProductIdAndAttributeId( 
			@RequestBody DeleteValueByProductIdAndAttributeIdDto request,
			@RequestParam(name = "includeDeleted", defaultValue = "false") boolean includeDeleted) {
		FindPValueByProductIdAndAttributeIdResponse response = _pValueService.findByProductIdAndAttributeId(request, includeDeleted);
		
		return ResponseEntity.ok(response);
	}

	@GetMapping("{id}")
	public ResponseEntity<FindPValueByIdResponse> findById(
			@PathVariable UUID id,
			@RequestParam(name = "includeDeleted", defaultValue = "false") boolean includeDeleted) {
		FindPValueByIdResponse response = _pValueService.findById(id, includeDeleted);

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("attribute_id")
	public ResponseEntity<List<FindPValuesByAttributeIdList>> findByAttributeIdList(
			@RequestBody List<UUID> attributeIdList,
			@RequestParam(name = "includeDeleted", defaultValue = "false") boolean includeDeleted) {
		List<FindPValuesByAttributeIdList> response = _pValueService.findAllByAttributeIdList(attributeIdList, includeDeleted);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("product_id")
	public ResponseEntity<List<FindPValuesByProductIdList>> findByProductIdList(
			@RequestBody List<UUID> productIdList,
			@RequestParam(name = "includeDeleted", defaultValue = "false") boolean includeDeleted) {
		List<FindPValuesByProductIdList> response = _pValueService.findAllByProductIdList(productIdList, includeDeleted);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("products")
	public ResponseEntity<List<FindAllProductByCategoryIdAndAttributeIdAndValueResponse>> findAllProductByCategoryIdAndAttributeIdValuePairs(
			@RequestBody FindAllProductByCategoryIdAndAttributeIdValueUnitTuplesDto request,
			@RequestParam(name = "includeDeleted", defaultValue = "false") boolean includeDeleted) {
		List<FindAllProductByCategoryIdAndAttributeIdAndValueResponse> response = 
				_pValueService.findAllProductByCategoryIdAndAttributeIdAndValue(request, includeDeleted);
		
		return ResponseEntity.ok(response);	
	}
	
	@PostMapping
	public ResponseEntity<Void> createValueById(
			@RequestBody 
			CreateValueByIdDto request) {
		_pValueService.createValueById(request);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	@PatchMapping("{id}")
	public ResponseEntity<Void> updateValueById(
			@PathVariable UUID id,
			@RequestBody 
			UpdateValueByIdDto request) {
		
		request.setId(id);
		_pValueService.updateValueById(request);
		
		return ResponseEntity.ok().build();
		
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteValueById(
			@PathVariable UUID id) {
		boolean delete = _pValueService.deleteValueById(id);
		
		return ResponseEntity.ok().build();
		
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteValueById(
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
