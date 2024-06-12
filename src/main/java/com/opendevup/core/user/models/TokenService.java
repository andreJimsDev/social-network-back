package com.opendevup.core.user.models;

import com.opendevup.core.user.gateways.TokenPersistenceGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenService {

    private final TokenPersistenceGateway tokenPersistenceGateway;

    public void revokeAllUserTokens(User user) {

        var validUserTokens = tokenPersistenceGateway.findAllValidTokenByUser(user);

        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(Token::validUserToken);
            tokenPersistenceGateway.updateAll(validUserTokens);
        }
    }

    public void saveUserToken(User user, String accessToken) {

        tokenPersistenceGateway.add(
                Token.builder()
                        .tokenId(tokenPersistenceGateway.next())
                        .user(user)
                        .accessToken(accessToken)
                        .tokenType(TokenType.BEARER)
                        .revoked(false)
                        .expired(false)
                        .build()
        );
    }
}
