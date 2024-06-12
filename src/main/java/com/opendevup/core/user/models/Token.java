package com.opendevup.core.user.models;

import com.opendevup.core.shared.model.EntityAudit;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Token extends EntityAudit {
    private TokenId tokenId;
    private String accessToken;
    private TokenType tokenType;
    private boolean expired;
    private boolean revoked;
    private User user;

    public void validUserToken() {
        expired = true;
        revoked = true;
    }

    public boolean isValid() {
        return !expired && !revoked;
    }
}
