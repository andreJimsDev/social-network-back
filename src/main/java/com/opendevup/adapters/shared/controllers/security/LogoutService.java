package com.opendevup.adapters.shared.controllers.security;

import com.opendevup.core.user.gateways.TokenPersistenceGateway;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenPersistenceGateway tokenPersistenceGateway;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedTokenOptional = tokenPersistenceGateway.findByAccessToken(jwt);
        if (storedTokenOptional.isPresent()) {
            var storedToken = storedTokenOptional.get();
            storedToken.validUserToken();
            tokenPersistenceGateway.add(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
