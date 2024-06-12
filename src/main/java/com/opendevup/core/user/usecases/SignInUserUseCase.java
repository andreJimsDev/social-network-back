package com.opendevup.core.user.usecases;

import com.opendevup.core.shared.model.Email;
import com.opendevup.core.shared.model.exceptions.EntityNotFoundException;
import com.opendevup.core.shared.model.exceptions.EntityNotFoundMessage;
import com.opendevup.core.shared.model.exceptions.UnauthorizedException;
import com.opendevup.core.shared.model.exceptions.UnauthorizedMessage;
import com.opendevup.core.shared.model.exceptions.ValidationInputException;
import com.opendevup.core.shared.model.exceptions.ValidationInputMessage;
import com.opendevup.core.user.gateways.AccessTokenService;
import com.opendevup.core.user.gateways.AuthenticationService;
import com.opendevup.core.user.gateways.HashGenerator;
import com.opendevup.core.user.models.TokenService;
import com.opendevup.core.user.models.User;
import com.opendevup.core.user.gateways.UserPersistenceGateway;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class SignInUserUseCase implements Function<SignInUserUseCase.Input, SignInUserUseCase.Output> {

    private final UserPersistenceGateway repository;
    private final AccessTokenService accessTokenService;
    private final HashGenerator hashGenerator;
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    @Override
    public Output apply(Input input) {
        Email email = Email.of(input.username);
        if (input.password() == null || input.password().isEmpty()) {
            throw new ValidationInputException(ValidationInputMessage.PASSWORD_IS_EMPTY);
        }

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundMessage.USER_IS_NOT_FOUND));


        boolean isPasswordRight = hashGenerator.compare(input.password(), user.getPassword());

        if (!isPasswordRight) {
            throw new UnauthorizedException(UnauthorizedMessage.PASSWORD_IS_WRONG);
        }

        authenticationService.authenticate(user.getUserId().id(), input.password());

        String accessToken = accessTokenService.generateToken(user);

        tokenService.revokeAllUserTokens(user);
        tokenService.saveUserToken(user, accessToken);

        return new Output(
                accessToken,
                accessTokenService.generateRefreshToken(user),
                user.fullName(),
                user.getRole().value()
        );
    }

    public record Input(String username, String password) {
    }

    public record Output(String token, String refreshToken, String fullName, String role) {
    }
}
