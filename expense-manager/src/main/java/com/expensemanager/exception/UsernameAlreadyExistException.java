package com.expensemanager.exception;

public class UsernameAlreadyExistException extends Exception {
     
    public UsernameAlreadyExistException() {}

    public UsernameAlreadyExistException(String msg) {

        super(msg);
    }
}
