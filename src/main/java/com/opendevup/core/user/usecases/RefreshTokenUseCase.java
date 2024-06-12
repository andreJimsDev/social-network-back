package com.opendevup.core.user.usecases;

import com.opendevup.core.shared.model.exceptions.EntityNotFoundMessage;
import com.opendevup.core.user.gateways.AccessTokenService;
import com.opendevup.core.user.models.TokenService;
import com.opendevup.core.user.models.UserId;
import com.opendevup.core.user.gateways.UserPersistenceGateway;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class RefreshTokenUseCase implements Function<RefreshTokenUseCase.Input, RefreshTokenUseCase.Output> {

    private final AccessTokenService accessTokenService;
    private final UserPersistenceGateway userPersistenceGateway;
    private final TokenService tokenService;

    @Override
    public Output apply(Input input) {
        final String authHeader = input.authHeader();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        final String refreshToken = authHeader.substring(7);
        var extractUserId = accessTokenService.extractUserId(refreshToken);

        if (extractUserId.isEmpty()) {
            return null;
        }

        final String userId = extractUserId.get();
        var user = userPersistenceGateway.findById(UserId.of(userId))
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundMessage.USER_IS_NOT_FOUND));

        if (!accessTokenService.isTokenValid(refreshToken, user)) {
            return null;
        }

        String accessToken = accessTokenService.generateToken(user);

        tokenService.revokeAllUserTokens(user);
        tokenService.saveUserToken(user, accessToken);

        return new Output(
                accessToken,
                refreshToken,
                user.fullName(),
                user.getRole().value()
        );
    }

    public record Input(String authHeader) {

    }

    public record Output(String token, String refreshToken, String fullName, String role) {
    }
}
