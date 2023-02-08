package com.suryansh.exception;

public class MicroserviceException extends RuntimeException{
    public MicroserviceException(String exMessage) {
        super("Exception from Product Microservice "+exMessage);
    }
}
