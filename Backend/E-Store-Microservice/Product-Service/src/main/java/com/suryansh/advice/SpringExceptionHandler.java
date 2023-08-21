package com.suryansh.advice;

import com.suryansh.exception.MicroserviceException;
import com.suryansh.exception.SpringInventoryException;
import com.suryansh.exception.SpringProductException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SpringExceptionHandler {
    @ExceptionHandler(MicroserviceException.class)
    public ResponseEntity<ErrorDesc> handleInvalidServerException(MicroserviceException ex){
        ErrorDesc.ErrorDetail errorDetail = new ErrorDesc.ErrorDetail(ex.getMessage(),
                "ServiceCommunicationError",HttpStatus.SERVICE_UNAVAILABLE.value()
                , Instant.now());
        ErrorDesc errorDesc = new ErrorDesc(errorDetail);
        return new ResponseEntity<>(errorDesc,HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SpringProductException.class)
    public Map<String,String>handleSpringProductException(SpringProductException e){
        Map<String,String>errorMap = new HashMap<>();
        errorMap.put("Exception", e.getMessage());
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
    @ExceptionHandler(SpringInventoryException.class)
    public ResponseEntity<ErrorDesc> handleSpringInventoryException(SpringInventoryException ex) {
        ErrorDesc.ErrorDetail errorDetail = new ErrorDesc.ErrorDetail(ex.getMessage(),
                ex.getType(), ex.getStatus().value(), Instant.now());
        ErrorDesc errorDesc = new ErrorDesc(errorDetail);
        return new ResponseEntity<>(errorDesc,ex.getStatus());
    }
    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ErrorDesc> handleSpringUnexpectedTypeException(UnexpectedTypeException ex) {
        ErrorDesc.ErrorDetail errorDetail = new ErrorDesc.ErrorDetail(ex.getMessage(),
                ex.getMessage(), HttpStatus.BAD_REQUEST.value(), Instant.now());
        ErrorDesc errorDesc = new ErrorDesc(errorDetail);
        return new ResponseEntity<>(errorDesc,HttpStatus.BAD_REQUEST);
    }
    public record ErrorDesc(ErrorDetail error) {
        public record ErrorDetail(String message, String type, int status, Instant timestamp) {
        }
    }
}
