package com.xgodness.itmodbcoursework.controller;

import java.sql.SQLException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SqlExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {SQLException.class})
    protected ResponseEntity<Object> handleConflict(SQLException ex, WebRequest request) {
        ex.printStackTrace();
        String bodyOfResponse = "You've committed war crime and caused internal server error...";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
