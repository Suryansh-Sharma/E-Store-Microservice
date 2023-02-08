package com.suryansh.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SpringOrderException extends RuntimeException {
    public SpringOrderException(String exMessage) {
        super(exMessage);
    }
}

