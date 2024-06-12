package com.opendevup.core.shared.model;

import com.opendevup.core.shared.model.exceptions.ValidationInputException;
import com.opendevup.core.shared.model.exceptions.ValidationInputMessage;

public record Email(String value) {
    private final static String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public Email {
        if (value == null || !value.matches(EMAIL_REGEX)) {
            throw new ValidationInputException(ValidationInputMessage.EMAIL_IS_INVALID);
        }
    }

    public static Email of(String email) {
        return new Email(email);
    }
}
