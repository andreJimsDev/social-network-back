package com.opendevup.adapters.user.controllers.config;

import com.opendevup.adapters.user.gateways.persistence.mysql.MySqlTokenPersistence;
import com.opendevup.adapters.user.gateways.persistence.mysql.MySqlUserPersistence;
import com.opendevup.core.user.gateways.AccessTokenService;
import com.opendevup.core.user.gateways.AuthenticationService;
import com.opendevup.core.user.gateways.HashGenerator;
import com.opendevup.core.user.usecases.RefreshTokenUseCase;
import com.opendevup.core.user.usecases.SignInUserUseCase;
import com.opendevup.core.user.usecases.SignUpUserUseCase;
import com.opendevup.core.user.models.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UserUseCaseConfig {

    private final AccessTokenService accessTokenService;
    private final HashGenerator hashGenerator;
    private final AuthenticationService authenticationService;
    private final MySqlUserPersistence userPersistence;
    private final MySqlTokenPersistence tokenPersistence;

    @Bean
    public SignInUserUseCase signInUserUseCase() {
        return new SignInUserUseCase(
                userPersistence,
                accessTokenService,
                hashGenerator,
                authenticationService,
                new TokenService(
                        tokenPersistence
                )
        );
    }

    @Bean
    public SignUpUserUseCase signUpUserUseCase() {
        return new SignUpUserUseCase(
                userPersistence,
                hashGenerator
        );
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase() {
        return new RefreshTokenUseCase(
                accessTokenService,
                userPersistence,
                new TokenService(
                        tokenPersistence
                )
        );
    }
}
