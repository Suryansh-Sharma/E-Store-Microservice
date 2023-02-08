package com.suryansh.advice;

import com.suryansh.exception.SpringInventoryException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SpringExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SpringInventoryException.class)
    public Map<String,String>handleSpringInventoryException(SpringInventoryException e){
        Map<String,String>errorMap=new HashMap<>();
        errorMap.put("Error Message from Inventory Service :- ", e.getMessage());
        return errorMap;
    }
}
