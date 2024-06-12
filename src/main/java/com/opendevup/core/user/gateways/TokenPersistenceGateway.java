package com.opendevup.core.user.gateways;

import com.opendevup.core.user.models.Token;
import com.opendevup.core.user.models.TokenId;
import com.opendevup.core.user.models.User;

import java.util.List;
import java.util.Optional;

public interface TokenPersistenceGateway {
    void add(Token token);

    List<Token> findAllValidTokenByUser(User user);

    void updateAll(List<Token> tokens);

    Optional<Token> findByAccessToken(String accessToken);

    TokenId next();
}
