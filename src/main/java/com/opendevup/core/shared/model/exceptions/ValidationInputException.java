package com.opendevup.core.shared.model.exceptions;

public class ValidationInputException extends RuntimeException {
    public ValidationInputException(String message) {
        super(message);
    }
}
