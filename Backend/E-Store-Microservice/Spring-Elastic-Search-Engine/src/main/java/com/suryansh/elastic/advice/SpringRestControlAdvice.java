package com.suryansh.elastic.advice;

import com.suryansh.elastic.exception.SpringElasticExcep;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SpringRestControlAdvice {
    @ExceptionHandler(SpringElasticExcep.class)
    public ResponseEntity<error> handleSpringElasticEc(SpringElasticExcep e){
        return ResponseEntity.status(e.getHttpStatus()).body(
                new error(
                        "Error",new error.Message(e.getMessage(),e.getType(),e.getHttpStatus())
                )
        );
    }
    public record error(String title,Message message){
        public record Message(String message, String type, HttpStatus status){}
    }
}
