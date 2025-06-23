package com.assignment.paymentservice.exception;

public class AccountAlreadyExistException extends RuntimeException {

    public AccountAlreadyExistException(String message) {
        super(message);
    }

}
