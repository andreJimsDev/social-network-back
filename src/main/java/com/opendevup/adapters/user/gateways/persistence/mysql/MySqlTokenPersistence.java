package com.opendevup.adapters.user.gateways.persistence.mysql;

import com.opendevup.adapters.user.gateways.persistence.mysql.jpa.JpaTokenEntityRepository;
import com.opendevup.adapters.user.gateways.persistence.mysql.mapper.TokenMapper;
import com.opendevup.core.user.models.Token;
import com.opendevup.core.user.models.TokenId;
import com.opendevup.core.user.models.User;
import com.opendevup.core.user.gateways.TokenPersistenceGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MySqlTokenPersistence implements TokenPersistenceGateway {

    private final JpaTokenEntityRepository jpaTokenEntityRepository;

    @Override
    public void add(Token token) {
        jpaTokenEntityRepository.save(
                TokenMapper.toEntity(token)
        );
    }

    @Override
    public List<Token> findAllValidTokenByUser(User user) {
        return jpaTokenEntityRepository.findAllValidTokenByUser(
                        user.getUserId().id()
                ).stream()
                .map(TokenMapper::toDomain)
                .toList();
    }

    @Override
    public void updateAll(List<Token> tokens) {
        jpaTokenEntityRepository.saveAll(
                tokens.stream()
                        .map(TokenMapper::toEntity)
                        .toList()
        );
    }

    @Override
    public Optional<Token> findByAccessToken(String accessToken) {
        return jpaTokenEntityRepository.findByAccessToken(accessToken)
                .map(TokenMapper::toDomain);
    }

    @Override
    public TokenId next() {
        return TokenId.of(UUID.randomUUID().toString());
    }
}
