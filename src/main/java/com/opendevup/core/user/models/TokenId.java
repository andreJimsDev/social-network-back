package com.opendevup.core.user.models;

import com.opendevup.core.shared.model.Guards;
import com.opendevup.core.shared.model.exceptions.ValidationInputMessage;

public record TokenId(String id) {

    public TokenId {
        Guards.notEmpty(id, ValidationInputMessage.TOKEN_ID_IS_EMPTY);
    }

    public static TokenId of(String id) {
        return new TokenId(id);
    }
}
