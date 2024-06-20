package com.boostech.demo.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such value") 
public class AttributeNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public AttributeNotFoundException() {}
	
	public AttributeNotFoundException(UUID value) {
		super(String.format("Not found attribute have id '%s'", value));
	}
	
}
