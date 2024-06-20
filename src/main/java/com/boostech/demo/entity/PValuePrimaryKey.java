package com.boostech.demo.entity;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Embeddable
public class PValuePrimaryKey implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "attribute_id")
	private Attribute attribute;
}
