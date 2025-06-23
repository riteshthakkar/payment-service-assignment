package com.assignment.paymentservice.exception;

public class DuplicatePaymentTransferException extends RuntimeException {
    public DuplicatePaymentTransferException(String message) {
        super(message);
    }
}
