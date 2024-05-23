package com.tRef.BankAccount.Exceptions;

public class LastPhoneNumberDeletionException extends RuntimeException {
    public LastPhoneNumberDeletionException() {
        super("Cannot delete last phone number");
    }
}