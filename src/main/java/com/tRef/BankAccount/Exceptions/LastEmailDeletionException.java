package com.tRef.BankAccount.Exceptions;

public class LastEmailDeletionException extends RuntimeException {
    public LastEmailDeletionException() {
        super("Cannot delete last email");
    }
}
