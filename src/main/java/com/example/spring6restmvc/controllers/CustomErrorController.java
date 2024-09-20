package com.example.spring6restmvc.controllers;

import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(TransactionSystemException.class)
    ResponseEntity handleTransactionSystemException(TransactionSystemException ex) {
        var responseBuilder = ResponseEntity.badRequest();

        if (ex.getCause() instanceof RollbackException) {
            if (ex.getCause().getCause() instanceof ConstraintViolationException cvEx) {

                var listErrors = cvEx.getConstraintViolations().stream().map(x -> {
                    var errorMap = new HashMap<String, String>();
                    errorMap.put(x.getPropertyPath().toString(), x.getMessage());
                    return errorMap;
                }).collect(Collectors.toList());
                return responseBuilder.body(listErrors);
            }
        }

        return responseBuilder.build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var listErrors = ex.getFieldErrors().stream().map(x -> {
            var errorMap = new HashMap<String, String>();
            errorMap.put(x.getField(), x.getDefaultMessage());
            return errorMap;
        }).collect(Collectors.toList());

        return ResponseEntity.badRequest().body(listErrors);
    }
}
