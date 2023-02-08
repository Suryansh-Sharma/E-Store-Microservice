package com.suryansh.reviewservice.exception;

public class MicroserviceException extends RuntimeException{
    public MicroserviceException(String message){
        super(message);
    }
}
