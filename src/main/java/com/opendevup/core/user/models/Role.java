package com.opendevup.core.user.models;

import com.opendevup.core.shared.model.exceptions.ValidationInputException;
import com.opendevup.core.shared.model.exceptions.ValidationInputMessage;

public record Role(String value) {

    public Role {
        if (!RoleConstant.ALL.contains(value))
            throw new ValidationInputException(ValidationInputMessage.ROLE_NOT_CONTAINS_THIS_ARGUMENT);
    }

    public static Role of(String value) {
        return new Role(value);
    }
}
