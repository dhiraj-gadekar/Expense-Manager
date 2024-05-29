package com.expensemanager.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensemanager.entity.User;
import com.expensemanager.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api")
public class UserController {
 
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{username}")
    public ResponseEntity<Optional<User>> getUserByUsername(@PathVariable("username") String username) {
        
        return ResponseEntity.ok().body(userRepository.findByUsername(username));
    }
    
}
