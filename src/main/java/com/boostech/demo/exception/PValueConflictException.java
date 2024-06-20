package com.boostech.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Value existed") 
public class PValueConflictException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PValueConflictException() {}
	
	public PValueConflictException(String message) {
		super(message);
	}
}
