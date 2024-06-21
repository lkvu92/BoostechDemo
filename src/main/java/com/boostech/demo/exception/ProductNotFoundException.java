package com.boostech.demo.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such value") 
public class ProductNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ProductNotFoundException() {}
	
	public ProductNotFoundException(UUID value) {
		super(String.format("Not found product have id '%s'", value));
	}
	
}
