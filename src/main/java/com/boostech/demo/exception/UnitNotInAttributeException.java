package com.boostech.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="unit not in attribute") 
public class UnitNotInAttributeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnitNotInAttributeException() {}
	
	public UnitNotInAttributeException(String message) {
		super(message);
	}
}
