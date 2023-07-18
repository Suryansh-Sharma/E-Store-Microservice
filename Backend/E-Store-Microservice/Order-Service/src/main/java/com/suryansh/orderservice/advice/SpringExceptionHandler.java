package com.suryansh.orderservice.advice;

import com.suryansh.orderservice.exception.MicroserviceException;
import com.suryansh.orderservice.exception.SpringOrderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SpringExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(SpringExceptionHandler.class);

    @ExceptionHandler(SpringOrderException.class)
    public ResponseEntity handleSpringOrderEx(SpringOrderException e){
        String m="Error Message from Order Service Microservice Communication : - "+ e.getMessage();
        return  ResponseEntity.status(e.getHttpStatus()).body(new
                ErrorMessage(m,e.getErrorCode(),e.getHttpStatus()));
    }
    @ExceptionHandler(MicroserviceException.class)
    public ResponseEntity<ErrorMessage>handleMicroserviceEx(MicroserviceException e){
        logger.error("Service is unavailable "+e);
        String m="Error Message from Order Service Microservice Communication : - "+ e.getMessage();
        return  ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new
                ErrorMessage(m,"MicroserviceCommunicationError",HttpStatus.SERVICE_UNAVAILABLE));
    }
    public record ErrorMessage(String message,String type,HttpStatus status){}
}
