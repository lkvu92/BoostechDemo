package com.boostech.demo.exception;

public class ErrorInfo {
    public final String message;
    public final int statusCode;

    public ErrorInfo(int statusCode, String message) {
    	this.statusCode = statusCode;
        this.message = message;
    }
}