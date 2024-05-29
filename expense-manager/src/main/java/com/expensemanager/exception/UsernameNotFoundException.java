package com.expensemanager.exception;

public class UsernameNotFoundException extends Exception {
    
    public UsernameNotFoundException() {}

    public UsernameNotFoundException(String msg) {

        super(msg);
    }
}
