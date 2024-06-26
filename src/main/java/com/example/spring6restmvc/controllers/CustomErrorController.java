package com.example.spring6restmvc.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorController {

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
