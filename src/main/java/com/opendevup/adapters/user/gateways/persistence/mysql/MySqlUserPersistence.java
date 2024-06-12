package com.opendevup.adapters.user.gateways.persistence.mysql;

import com.opendevup.adapters.user.gateways.persistence.mysql.jpa.JpaUserEntityRepository;
import com.opendevup.adapters.user.gateways.persistence.mysql.mapper.UserMapper;
import com.opendevup.core.shared.model.Email;
import com.opendevup.core.user.models.User;
import com.opendevup.core.user.models.UserId;
import com.opendevup.core.user.gateways.UserPersistenceGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MySqlUserPersistence implements UserPersistenceGateway {

    private final JpaUserEntityRepository jpaUserEntityRepository;

    @Override
    public void add(User user) {
        jpaUserEntityRepository.save(
                UserMapper.toEntity(user)
        );
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaUserEntityRepository.findByEmail(
                email.value()
        ).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return jpaUserEntityRepository.findById(
                userId.id()
        ).map(UserMapper::toDomain);
    }

    @Override
    public UserId next() {
        return UserId.of(UUID.randomUUID().toString());
    }
}
