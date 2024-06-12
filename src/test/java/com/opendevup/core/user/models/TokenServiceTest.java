package com.opendevup.core.user.models;

import com.opendevup.adapters.user.gateways.persistence.fake.InMemoryTokenPersistence;
import com.opendevup.adapters.user.gateways.persistence.fake.InMemoryUserPersistence;
import com.opendevup.core.shared.model.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenServiceTest {
    private InMemoryTokenPersistence tokenRepository;
    private InMemoryUserPersistence userRepository;

    @BeforeEach
    public void setup() {
        tokenRepository = new InMemoryTokenPersistence();
        userRepository = new InMemoryUserPersistence();
    }

    @Test
    public void shouldRevokeAllUserTokensWhenHasTokenExist() {

        var user = User
                .builder()
                .userId(userRepository.next())
                .firstName("John")
                .lastName("Doe")
                .email(Email.of("user@test.ts"))
                .password("hashed:my-strong-password")
                .role(Role.of(RoleConstant.USER))
                .build();

        tokenRepository.add(
                Token.builder()
                        .tokenId(tokenRepository.next())
                        .user(user)
                        .accessToken("accessToken")
                        .tokenType(TokenType.BEARER)
                        .revoked(false)
                        .expired(false)
                        .build()
        );

        TokenService tokenService = new TokenService(
                tokenRepository
        );

        tokenService.revokeAllUserTokens(user);

        var store = tokenRepository.getStore();

        assertNotNull(store);
        assertEquals(store.size(), 1);
        var token = store.get(tokenRepository.next());
        assertNotNull(token);
        assertTrue(token.isRevoked());
        assertTrue(token.isExpired());
    }


    @Test
    public void shouldSaveUserToken() {
        var user = User
                .builder()
                .userId(userRepository.next())
                .firstName("John")
                .lastName("Doe")
                .email(Email.of("user@test.ts"))
                .password("hashed:my-strong-password")
                .role(Role.of(RoleConstant.USER))
                .build();

        TokenService tokenService = new TokenService(
                tokenRepository
        );

        tokenService.saveUserToken(user, "accessToken");

        var store = tokenRepository.getStore();

        assertNotNull(store);
        assertEquals(store.size(), 1);
        var token = store.get(tokenRepository.next());
        assertNotNull(token);
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());
        assertEquals(token.getTokenType(), TokenType.BEARER);
        assertEquals(token.getTokenId(), tokenRepository.next());
        assertEquals(user.getUserId(), userRepository.next());
    }

}
