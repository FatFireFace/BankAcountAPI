package com.tRef.BankAccount.Exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email already exists");
    }
}
