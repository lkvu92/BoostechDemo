package com.boostech.demo.dto;

import java.util.UUID;

public class AttributeIdValueUnitTuple {
	 public UUID id;
	 public String value;
	 
	 public AttributeIdValueUnitTuple() {}
	 
	 public AttributeIdValueUnitTuple(UUID id, String value, UUID unitId) {
        this.id = id;
        this.value = value;
  
	 }
}