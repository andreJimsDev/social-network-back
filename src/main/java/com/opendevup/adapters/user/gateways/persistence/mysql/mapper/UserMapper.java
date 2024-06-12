package com.opendevup.adapters.user.gateways.persistence.mysql.mapper;

import com.opendevup.adapters.user.gateways.persistence.mysql.jpa.JpaUserEntity;
import com.opendevup.core.shared.model.Email;
import com.opendevup.core.user.models.Role;
import com.opendevup.core.user.models.User;
import com.opendevup.core.user.models.UserId;

public class UserMapper {
    public static JpaUserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        } else {
            JpaUserEntity jpaUserEntity = new JpaUserEntity();

            jpaUserEntity.setId(domain.getUserId().id());
            jpaUserEntity.setFirstName(domain.getFirstName());
            jpaUserEntity.setLastName(domain.getLastName());
            jpaUserEntity.setEmail(domain.getEmail().value());
            jpaUserEntity.setPassword(domain.getPassword());
            jpaUserEntity.setRole(domain.getRole().value());

            return jpaUserEntity;
        }
    }

    public static User toDomain(JpaUserEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return User.builder()
                    .userId(UserId.of(entity.getId()))
                    .firstName(entity.getFirstName())
                    .lastName(entity.getLastName())
                    .email(Email.of(entity.getEmail()))
                    .password(entity.getPassword())
                    .role(Role.of(entity.getRole()))
                    .build();
        }
    }
}
