package com.suryansh.orderservice.exception;

public class MicroserviceException extends RuntimeException{
    public MicroserviceException(String message){
        super(message);
    }
}
