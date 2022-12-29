package com.suryansh.orderservice.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SpringOrderException extends RuntimeException {
    public SpringOrderException(String exMessage,HttpStatus httpStatus) {
        super();
    }
}

