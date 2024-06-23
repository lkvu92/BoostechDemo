package com.boostech.demo.controller;

import com.boostech.demo.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseController {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({PValueNotFoundException.class, ProductNotFoundException.class, AttributeNotFoundException.class, UnitNotFoundException.class})
    @ResponseBody
    ErrorInfo handlePValueNotFound(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(HttpStatus.NOT_FOUND.value(), ex.getLocalizedMessage());
    }
}
