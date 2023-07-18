package com.suryansh.exception;


import org.springframework.http.HttpStatus;

public class SpringInventoryException extends RuntimeException {
    private final String type;
    private final HttpStatus status;

    public SpringInventoryException(String exMessage, String type, HttpStatus status) {
        super(exMessage);
        this.type = type;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public HttpStatus getStatus() {
        return status;
    }
}