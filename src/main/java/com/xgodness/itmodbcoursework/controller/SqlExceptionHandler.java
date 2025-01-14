package com.xgodness.itmodbcoursework.controller;

import java.sql.SQLException;

import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log
@ControllerAdvice
public class SqlExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String ERROR_500_MSG = "Something went wrong";

    @ExceptionHandler(value = {SQLException.class})
    protected ResponseEntity<Object> handleConflict(SQLException ex, WebRequest request) {
        log.severe("[EXCEPTION] got SQLException: " + ex.getMessage());
        return handleExceptionInternal(ex, ERROR_500_MSG, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
