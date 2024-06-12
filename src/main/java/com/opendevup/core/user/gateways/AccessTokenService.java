package com.opendevup.core.user.gateways;

import com.opendevup.core.user.models.User;

import java.util.Optional;

public interface AccessTokenService {
    String generateToken(User user);
    String generateRefreshToken(User user);
    Optional<String> extractUserId(String refreshToken);
    boolean isTokenValid(String refreshToken, User user);
}
