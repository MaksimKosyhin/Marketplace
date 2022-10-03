package com.marketplace.config.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {NonExistingEntityException.class})
    public ResponseEntity<?> handle(NonExistingEntityException ex, WebRequest request) {
        return super.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {ParentCategoryException.class})
    public ResponseEntity<?> handle(ParentCategoryException ex, WebRequest request) {
        return super.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {AddEntryException.class})
    public ResponseEntity<?> handle(AddEntryException ex, WebRequest request) {
        return super.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {ModifyingEntryException.class})
    public ResponseEntity<?> handle(ModifyingEntryException ex, WebRequest request) {
        return super.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {RequestException.class})
    public ResponseEntity<?> handle(RequestException ex, WebRequest request) {
        return super.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
