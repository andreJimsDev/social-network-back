package com.opendevup.adapters.user.gateways.authentication;

import com.opendevup.core.user.gateways.AuthenticationService;
import com.opendevup.core.user.models.User;
import com.opendevup.core.user.models.UserId;
import lombok.Getter;

@Getter
public class AuthenticationInMemoryService implements AuthenticationService {

    private User user;

    @Override
    public void authenticate(String username, String password) {
        user = User.builder()
                .userId(UserId.of(username))
                .password(password)
                .build();
    }
}
