package com.boostech.demo.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomResponse class
 * @param <T>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse<T> {
    private String message;
    private int status;
    private T data;
}
