package com.boostech.demo.exception.attribute;

public class AttributeAlreadyAddedException extends RuntimeException {
    public AttributeAlreadyAddedException(String message) {
        super(message);
    }
}