package com.expensemanager.exceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.expensemanager.exception.LoginCredentialWrongException;
import com.expensemanager.exception.NullValueException;
import com.expensemanager.exception.UsernameAlreadyExistException;
import com.expensemanager.exception.UsernameNotFoundException;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(LoginCredentialWrongException.class)
    public ResponseEntity<ErrorMessage> loginCredentialWrongException(LoginCredentialWrongException exception,
            WebRequest request) {
        ErrorMessage message = new ErrorMessage(LocalDateTime.now().toString(), HttpStatus.UNAUTHORIZED, exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(NullValueException.class)
    public ResponseEntity<ErrorMessage> nullValueException(NullValueException exception,
            WebRequest request) {
        ErrorMessage message = new ErrorMessage(LocalDateTime.now().toString(), HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
    
    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> usernameAlreadyExistException(UsernameAlreadyExistException exception,
            WebRequest request) {
        ErrorMessage message = new ErrorMessage(LocalDateTime.now().toString(), HttpStatus.ALREADY_REPORTED, exception.getMessage());

        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(message);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessage> noSuchSupplierException(UsernameNotFoundException exception,
            WebRequest request) {
        ErrorMessage message = new ErrorMessage(LocalDateTime.now().toString(), HttpStatus.NOT_FOUND, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}