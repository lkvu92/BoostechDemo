package com.boostech.demo.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such value") 
public class UnitNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnitNotFoundException() {}
	
	public UnitNotFoundException(UUID value) {
		super(String.format("Not found unit have id '%s'", value));
	}
}
