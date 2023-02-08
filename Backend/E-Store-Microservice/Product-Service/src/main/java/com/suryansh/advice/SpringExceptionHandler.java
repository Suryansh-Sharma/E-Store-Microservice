package com.suryansh.advice;

import com.suryansh.exception.MicroserviceException;
import com.suryansh.exception.SpringProductException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SpringExceptionHandler {
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MicroserviceException.class)
    public Map<String,String>handleInvalidServerException(MicroserviceException e){
        Map<String,String>errorMap = new HashMap<>();
        errorMap.put("Exception Message", e.getMessage());
        return errorMap;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SpringProductException.class)
    public Map<String,String>handleSpringProductException(SpringProductException e){
        Map<String,String>errorMap = new HashMap<>();
        errorMap.put("Exception Message", e.getMessage());
        return errorMap;
    }
}
