package com.opendevup.adapters.user.gateways.persistence.fake;

import com.opendevup.core.user.models.Token;
import com.opendevup.core.user.models.TokenId;
import com.opendevup.core.user.gateways.TokenPersistenceGateway;
import com.opendevup.core.user.models.User;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class InMemoryTokenPersistence implements TokenPersistenceGateway {

    private final Map<TokenId, Token> store = new HashMap<>();

    @Override
    public void add(Token token) {
        store.put(token.getTokenId(), token);
    }

    @Override
    public List<Token> findAllValidTokenByUser(User user) {
        return store.values()
                .stream()
                .filter(token -> Objects.requireNonNull(token.getUser()).getUserId().equals(user.getUserId()))
                .filter(token -> !token.isExpired() || !token.isRevoked())
                .collect(Collectors.toList());
    }

    @Override
    public void updateAll(List<Token> tokens) {
        tokens.forEach(this::add);
    }

    @Override
    public Optional<Token> findByAccessToken(String accessToken) {
        return store.values()
                .stream()
                .filter(token -> accessToken.equals(token.getAccessToken()))
                .findFirst();
    }

    @Override
    public TokenId next() {
        return TokenId.of("c1ac0bbb-1b52-4903-8141-9a52f979fb22");
    }
}
