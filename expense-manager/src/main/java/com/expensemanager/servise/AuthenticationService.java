package com.expensemanager.servise;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expensemanager.entity.User;
import com.expensemanager.exception.LoginCredentialWrongException;
import com.expensemanager.exception.NullValueException;
import com.expensemanager.exception.UsernameAlreadyExistException;
import com.expensemanager.models.AuthenticationRequest;
import com.expensemanager.models.AuthenticationResponse;
import com.expensemanager.repository.UserRepository;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public AuthenticationResponse signUp(User request) throws NullValueException, UsernameAlreadyExistException {

        if (Objects.isNull(request.getUsername()))
            throw new NullValueException("Username can't be null");

        if (Objects.isNull(request.getPassword()))
            throw new NullValueException("Password is null");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (userService.isUsernameExists(request.getUsername())) {

            throw new UsernameAlreadyExistException("Username already Used");
        }
        user = userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(LocalDateTime.now().toString(), token);
    }

    public AuthenticationResponse logIn(AuthenticationRequest request) throws LoginCredentialWrongException {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(
                        () -> new LoginCredentialWrongException("Invalid Email or password. Please Enter valid Credentials..!"));

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {

            throw new LoginCredentialWrongException("Invalid Email or password. Please Enter valid Credentials..!");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(LocalDateTime.now().toString(), token);
    }
}

