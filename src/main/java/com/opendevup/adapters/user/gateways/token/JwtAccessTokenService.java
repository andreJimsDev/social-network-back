package com.opendevup.adapters.user.gateways.token;

import com.opendevup.core.user.gateways.AccessTokenService;
import com.opendevup.core.user.models.User;
import com.opendevup.adapters.shared.controllers.security.JwtService;
import com.opendevup.adapters.user.gateways.persistence.mysql.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtAccessTokenService implements AccessTokenService {

    private final JwtService jwtService;

    @Override
    public String generateToken(User user) {
        return jwtService.generateToken(
                UserMapper.toEntity(user)
        );
    }

    @Override
    public String generateRefreshToken(User user) {
        return jwtService.generateRefreshToken(
                UserMapper.toEntity(user)
        );
    }

    @Override
    public Optional<String> extractUserId(String refreshToken) {
        return Optional.ofNullable(
                jwtService.extractUsername(refreshToken)
        );
    }

    @Override
    public boolean isTokenValid(String refreshToken, User user) {
        return jwtService.isTokenValid(refreshToken, UserMapper.toEntity(user));
    }
}
