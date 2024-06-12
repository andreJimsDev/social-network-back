package com.opendevup.adapters.user.gateways.persistence.mysql.mapper;

import com.opendevup.adapters.user.gateways.persistence.mysql.jpa.JpaTokenEntity;
import com.opendevup.core.shared.model.exceptions.UnauthorizedException;
import com.opendevup.core.shared.model.exceptions.UnauthorizedMessage;
import com.opendevup.core.user.models.Token;
import com.opendevup.core.user.models.TokenId;

public class TokenMapper {
    public static JpaTokenEntity toEntity(Token domain) {
        if (domain == null) {
            return null;
        } else {
            if (domain.getTokenId() == null)
                throw new UnauthorizedException(UnauthorizedMessage.ACCESS_DENIED);

            JpaTokenEntity jpaTokenEntity = new JpaTokenEntity();

            jpaTokenEntity.setId(domain.getTokenId().id());
            jpaTokenEntity.setAccessToken(domain.getAccessToken());
            jpaTokenEntity.setTokenType(domain.getTokenType());
            jpaTokenEntity.setExpired(domain.isExpired());
            jpaTokenEntity.setRevoked(domain.isRevoked());

            jpaTokenEntity.setUser(
                    UserMapper.toEntity(
                            domain.getUser()
                    )
            );

            return jpaTokenEntity;
        }
    }

    public static Token toDomain(JpaTokenEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return Token.builder()
                    .tokenId(TokenId.of(entity.getId()))
                    .accessToken(entity.getAccessToken())
                    .tokenType(entity.getTokenType())
                    .expired(entity.isExpired())
                    .revoked(entity.isRevoked())
                    .build();
        }
    }
}