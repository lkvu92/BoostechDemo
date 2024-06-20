package com.boostech.demo.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such value") 
public class PValueNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public PValueNotFoundException() {}
	
	public PValueNotFoundException(UUID id) {
		super(String.format("Not found value have id '%s'", id));
	}
	
	public PValueNotFoundException(String fieldName, UUID value) {
		super(String.format("Not found value have %s '%s'", fieldName, value));
	}
	
    public PValueNotFoundException(UUID attributeId, UUID productId) {
        super(String.format("Not found value have attributeId '%s' and productId '%s'", attributeId, productId));
    }
}
