package com.opendevup.core.user.models;

import com.opendevup.core.shared.model.Guards;
import com.opendevup.core.shared.model.exceptions.ValidationInputMessage;

public record UserId(String id) {

    public UserId {
        Guards.notEmpty(id, ValidationInputMessage.USER_ID_IS_EMPTY);
    }

    public static UserId of(String id) {
        return new UserId(id);
    }
}
