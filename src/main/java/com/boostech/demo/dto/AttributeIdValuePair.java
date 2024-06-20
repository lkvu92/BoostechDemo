package com.boostech.demo.dto;

import java.util.UUID;

public class AttributeIdValuePair {
	 public UUID id;
	 public String value;
	 
	 public AttributeIdValuePair() {}
	 
	 public AttributeIdValuePair(UUID id, String value) {
        this.id = id;
        this.value = value;
	 }
}