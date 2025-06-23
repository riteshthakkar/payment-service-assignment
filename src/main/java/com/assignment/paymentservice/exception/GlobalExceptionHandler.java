package com.assignment.paymentservice.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {AccountAlreadyExistException.class})
    public ResponseEntity<?> handleAccountAlreadyExistException(AccountAlreadyExistException accountAlreadyExistException){
        ExceptionPayload exceptionPayload = new ExceptionPayload(
                accountAlreadyExistException.getMessage(),
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(exceptionPayload,exceptionPayload.getHttpStatus());
    }

    @ExceptionHandler(value = {AccountNotFoundException.class})
    public ResponseEntity<?> handleAccountNotFoundException(AccountNotFoundException accountNotFoundException){
        ExceptionPayload exceptionPayload = new ExceptionPayload(
                accountNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(exceptionPayload,exceptionPayload.getHttpStatus());
    }

    @ExceptionHandler(value = {InsufficientBalanceException .class})
    public ResponseEntity<?> handleInsufficientBalanceException(InsufficientBalanceException insufficientBalanceException){
        ExceptionPayload exceptionPayload = new ExceptionPayload(
                insufficientBalanceException.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(exceptionPayload,exceptionPayload.getHttpStatus());
    }

    @ExceptionHandler(value = {DuplicatePaymentTransferException.class})
    public ResponseEntity<?> handleDuplicatePaymentTransferException(DuplicatePaymentTransferException duplicatePaymentTransferException){
        ExceptionPayload exceptionPayload = new ExceptionPayload(
                duplicatePaymentTransferException.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(exceptionPayload,exceptionPayload.getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidFormat(Exception ex) {
        ExceptionPayload exceptionPayload = new ExceptionPayload(
                "Invalid input format",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(exceptionPayload,exceptionPayload.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericError(Exception ex) {
        ExceptionPayload exceptionPayload = new ExceptionPayload(
                "Unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(exceptionPayload,exceptionPayload.getHttpStatus());
    }
}
