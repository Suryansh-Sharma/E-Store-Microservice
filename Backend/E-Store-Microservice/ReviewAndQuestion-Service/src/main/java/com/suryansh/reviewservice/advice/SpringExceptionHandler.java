package com.suryansh.reviewservice.advice;

import com.suryansh.reviewservice.exception.MicroserviceException;
import com.suryansh.reviewservice.exception.SpringReviewException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SpringExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SpringReviewException.class)
    public Map<String,String>handleSpringReviewException(SpringReviewException e){
        Map<String,String>errorMap=new HashMap<>();
        errorMap.put("Spring ReviewService Exception:- ",e.getMessage());
        return errorMap;
    }
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MicroserviceException.class)
    public Map<String,String>handleMicroserviceException(MicroserviceException e){
        Map<String,String>errorMap=new HashMap<>();
        errorMap.put("Spring ReviewService Microservice Communication Exception:- ",e.getMessage());
        return errorMap;
    }
}
