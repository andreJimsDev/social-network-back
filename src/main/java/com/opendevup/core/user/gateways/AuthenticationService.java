package com.opendevup.core.user.gateways;

public interface AuthenticationService {
    void authenticate(String username, String password);
}
