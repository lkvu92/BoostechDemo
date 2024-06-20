package com.boostech.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "p_value")
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
public class PValue extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "attribute_id")
	private Attribute attribute;
	
    @Column(name = "value")
	private String value;
}
