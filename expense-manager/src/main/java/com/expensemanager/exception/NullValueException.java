package com.expensemanager.exception;

public class NullValueException extends Exception {
    
    public NullValueException() {}

    public NullValueException(String msg) {

        super(msg);
    }
}
