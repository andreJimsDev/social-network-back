package com.opendevup.core.shared.model.exceptions;

import java.io.Serial;

public class UnauthorizedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String errorMessage) {
        super(errorMessage);
    }
}
