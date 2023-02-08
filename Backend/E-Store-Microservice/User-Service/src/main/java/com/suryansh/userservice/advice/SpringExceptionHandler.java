package com.suryansh.userservice.advice;

import com.suryansh.userservice.exception.MicroserviceException;
import com.suryansh.userservice.exception.UserServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SpringExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserServiceException.class)
    public Map<String,String>handleUserServiceException(UserServiceException e){
        Map<String,String>errorMap=new HashMap<>();
        errorMap.put("Exception from User Service : - ",e.getMessage());
        return errorMap;
    }
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(MicroserviceException.class)
    public Map<String,String>handleMicroserviceException(MicroserviceException e){
        Map<String,String>errorMap=new HashMap<>();
        errorMap.put("Exception from User Service Communication: - ",e.getMessage());
        return errorMap;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(
                error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }
}
