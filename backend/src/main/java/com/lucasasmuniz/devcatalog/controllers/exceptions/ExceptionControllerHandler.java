package com.lucasasmuniz.devcatalog.controllers.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lucasasmuniz.devcatalog.services.exceptions.DatabaseException;
import com.lucasasmuniz.devcatalog.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionControllerHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardException> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardException err = new StandardException();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardException> database(DatabaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardException err = new StandardException();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
