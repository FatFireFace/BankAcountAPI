package com.tRef.BankAccount.Exceptions;

public class PhoneNumberAlreadyExistsException extends RuntimeException {
    public PhoneNumberAlreadyExistsException() {
        super("Phone number already exists");
    }
}