package com.suryansh.orderservice.advice;

import com.suryansh.orderservice.exception.MicroserviceException;
import com.suryansh.orderservice.exception.SpringOrderException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SpringExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SpringOrderException.class)
    public Map<String,String>handleSpringOrderEx(SpringOrderException e){
        Map<String,String>errorMap=new HashMap<>();
        errorMap.put("Error Message from Order Service: - ", e.getMessage());
        return errorMap;
    }
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MicroserviceException.class)
    public Map<String,String>handleMicroserviceEx(MicroserviceException e){
        Map<String,String>errorMap=new HashMap<>();
        errorMap.put("Error Message from Order Service Microservice Communication : - ", e.getMessage());
        return errorMap;
    }
}
