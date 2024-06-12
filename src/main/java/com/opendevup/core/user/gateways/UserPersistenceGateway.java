package com.opendevup.core.user.gateways;

import com.opendevup.core.shared.model.Email;
import com.opendevup.core.user.models.User;
import com.opendevup.core.user.models.UserId;

import java.util.Optional;

public interface UserPersistenceGateway {
    void add(User user);

    Optional<User> findByEmail(Email email);

    Optional<User> findById(UserId id);

    UserId next();
}
