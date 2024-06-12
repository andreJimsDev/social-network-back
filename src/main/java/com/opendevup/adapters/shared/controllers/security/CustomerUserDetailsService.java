package com.opendevup.adapters.shared.controllers.security;

import com.opendevup.adapters.user.gateways.persistence.mysql.mapper.UserMapper;
import com.opendevup.core.shared.model.exceptions.EntityNotFoundMessage;
import com.opendevup.core.user.models.UserId;
import com.opendevup.core.user.gateways.UserPersistenceGateway;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserPersistenceGateway userPersistenceGateway;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return userPersistenceGateway.findById(UserId.of(id))
                .map(UserMapper::toEntity)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                EntityNotFoundMessage.USER_IS_NOT_FOUND
                        )
                );
    }
}
