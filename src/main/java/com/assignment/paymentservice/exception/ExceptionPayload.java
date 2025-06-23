package com.assignment.paymentservice.exception;

import org.springframework.http.HttpStatus;

public class ExceptionPayload {

    private final String message;
    private final HttpStatus httpStatus;

    public ExceptionPayload(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
