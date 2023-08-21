package com.suryansh.elastic.exception;

import org.springframework.http.HttpStatus;

public class SpringElasticExcep extends RuntimeException{
    private String type;
    private HttpStatus httpStatus;

    public String getType() {
        return type;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public SpringElasticExcep(String message, String type, HttpStatus httpStatus) {
        super(message);
        this.httpStatus=httpStatus;
        this.type=type;
    }

}
