package com.banew.cinema_server.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.banew.cinema_server.backend.dto.SingleStringResponse;
import com.banew.cinema_server.backend.exceptions.BadRequestSendedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static String VALIDATION_ERROR_MESSAGE = "Введені дані некоректні або відсутні!";

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestSendedException.class)
    public SingleStringResponse badRequestEx(BadRequestSendedException ex) {
        return new SingleStringResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SingleStringResponse validationExc(MethodArgumentNotValidException ex) {
        return new SingleStringResponse(VALIDATION_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
