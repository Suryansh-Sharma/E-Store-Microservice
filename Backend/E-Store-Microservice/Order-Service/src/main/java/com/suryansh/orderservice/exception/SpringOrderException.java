package com.suryansh.orderservice.exception;

import org.springframework.http.HttpStatus;

public class SpringOrderException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;
    public SpringOrderException(String exMessage,String errorCode,HttpStatus status) {
        super(exMessage);
        this.errorCode=errorCode;
        this.httpStatus=status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

}

