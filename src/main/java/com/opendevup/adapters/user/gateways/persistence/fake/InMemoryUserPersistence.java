package com.opendevup.adapters.user.gateways.persistence.fake;

import com.opendevup.core.shared.model.Email;
import com.opendevup.core.user.models.User;
import com.opendevup.core.user.models.UserId;
import com.opendevup.core.user.gateways.UserPersistenceGateway;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class InMemoryUserPersistence implements UserPersistenceGateway {

    private final Map<UserId, User> store = new HashMap<>();

    @Override
    public void add(User user) {
        store.put(user.getUserId(), user);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return store.values()
                .stream()
                .filter(userDto -> email.equals(userDto.getEmail()))
                .findFirst();
    }

    @Override
    public Optional<User> findById(UserId id) {
        return store.values()
                .stream()
                .filter(userDto -> id.equals(userDto.getUserId()))
                .findFirst();
    }

    @Override
    public UserId next() {
        return UserId.of("61e878a4-f7d4-465d-b801-196d6e52a556");
    }
}
