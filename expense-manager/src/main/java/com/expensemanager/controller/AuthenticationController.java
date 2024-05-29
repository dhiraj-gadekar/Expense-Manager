package com.expensemanager.controller;

import org.springframework.web.bind.annotation.RestController;

import com.expensemanager.entity.User;
import com.expensemanager.exception.LoginCredentialWrongException;
import com.expensemanager.exception.NullValueException;
import com.expensemanager.exception.UsernameAlreadyExistException;
import com.expensemanager.models.AuthenticationRequest;
import com.expensemanager.models.AuthenticationResponse;
import com.expensemanager.servise.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody User user) throws NullValueException, UsernameAlreadyExistException {
        
        return ResponseEntity.ok().body(authenticationService.signUp(user));
    }  

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) throws LoginCredentialWrongException {
        
        return ResponseEntity.ok().body(authenticationService.logIn(authenticationRequest));
    } 
}
