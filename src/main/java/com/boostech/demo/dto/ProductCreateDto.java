package com.boostech.demo.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductCreateDto {
    private String name;
    private UUID cate_id;
}
