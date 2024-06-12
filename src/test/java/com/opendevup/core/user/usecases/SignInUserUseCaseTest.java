package com.opendevup.core.user.usecases;

import com.opendevup.core.shared.model.Email;
import com.opendevup.core.shared.model.exceptions.EntityNotFoundException;
import com.opendevup.core.shared.model.exceptions.EntityNotFoundMessage;
import com.opendevup.core.shared.model.exceptions.UnauthorizedException;
import com.opendevup.core.shared.model.exceptions.UnauthorizedMessage;
import com.opendevup.core.shared.model.exceptions.ValidationInputException;
import com.opendevup.core.shared.model.exceptions.ValidationInputMessage;
import com.opendevup.core.user.models.Role;
import com.opendevup.adapters.user.gateways.authentication.AuthenticationInMemoryService;
import com.opendevup.adapters.user.gateways.hash.DeterministicHashGenerator;
import com.opendevup.adapters.user.gateways.persistence.fake.InMemoryTokenPersistence;
import com.opendevup.adapters.user.gateways.persistence.fake.InMemoryUserPersistence;
import com.opendevup.core.user.gateways.AccessTokenService;
import com.opendevup.core.user.models.RoleConstant;
import com.opendevup.core.user.models.User;
import com.opendevup.core.user.models.TokenService;
import com.opendevup.core.user.models.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SignInUserUseCaseTest {

    private DeterministicHashGenerator hashGenerator;
    private InMemoryUserPersistence userPersistence;
    private InMemoryTokenPersistence tokenPersistence;
    private SignInUserUseCase useCase;
    private AuthenticationInMemoryService authenticationService;

    @BeforeEach
    public void setup() {
        hashGenerator = new DeterministicHashGenerator("hashed");
        userPersistence = new InMemoryUserPersistence();
        tokenPersistence = new InMemoryTokenPersistence();
        authenticationService = new AuthenticationInMemoryService();
        AccessTokenService accessTokenService = new AccessTokenService() {
            @Override
            public String generateToken(User user) {
                return "signed-jwt-token";
            }

            @Override
            public String generateRefreshToken(User user) {
                return "refresh-signed-jwt-token";
            }

            @Override
            public Optional<String> extractUserId(String refreshToken) {
                return Optional.empty();
            }

            @Override
            public boolean isTokenValid(String refreshToken, User user) {
                return false;
            }
        };
        useCase = new SignInUserUseCase(
                userPersistence,
                accessTokenService,
                hashGenerator,
                authenticationService,
                new TokenService(
                        tokenPersistence
                )
        );
    }

    @Test
    public void shouldCannotLoginWithoutEmail() {
        var input = new SignInUserUseCase.Input(
                null,
                "userTestPassword"
        );
        ValidationInputException exception = assertThrows(ValidationInputException.class, () -> useCase.apply(input));

        assertEquals(ValidationInputMessage.EMAIL_IS_INVALID, exception.getMessage());
    }

    @Test
    public void shouldCannotLoginWithoutPassword() {
        var input = new SignInUserUseCase.Input(
                "user@test.ts",
                null
        );
        ValidationInputException exception = assertThrows(ValidationInputException.class, () -> useCase.apply(input));

        assertEquals(ValidationInputMessage.PASSWORD_IS_EMPTY, exception.getMessage());
    }

    @Test
    public void shouldCannotLoginIfUserDoesNotExist() {
        var input = new SignInUserUseCase.Input(
                "test@yopmail.com",
                "userTestPassword"
        );
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.apply(input));

        assertEquals(EntityNotFoundMessage.USER_IS_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void shouldCannotLoginIfPasswordIsWrong() {
        var input = new SignInUserUseCase.Input(
                "user@test.ts",
                "userTestPassword"
        );

        var user = User
                .builder()
                .userId(UserId.of("608fb1d0-23be-4885-a0e7-b02e3c8c796f"))
                .firstName("John")
                .lastName("Doe")
                .email(Email.of("user@test.ts"))
                .password("hashed:my-strong-password")
                .role(Role.of(RoleConstant.USER))
                .build();
        userPersistence.add(user);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> useCase.apply(input));

        assertEquals(UnauthorizedMessage.PASSWORD_IS_WRONG, exception.getMessage());
    }

    @Test
    public void shouldCanLogin() {
        String password = "my-strong-password";
        String hashedPassword = hashGenerator.generate(password);

        var input = new SignInUserUseCase.Input(
                "user@test.ts",
                password
        );

        var user = User
                .builder()
                .userId(userPersistence.next())
                .firstName("John")
                .lastName("Doe")
                .email(Email.of("user@test.ts"))
                .password(hashedPassword)
                .role(Role.of(RoleConstant.USER))
                .build();

        userPersistence.add(user);

        var output = useCase.apply(input);

        var userAuthenticated = authenticationService.getUser();

        assertNotNull(userAuthenticated);
        assertNotNull(userAuthenticated.getUserId());
        assertEquals(userAuthenticated.getUserId(), user.getUserId());
        assertNotNull(tokenPersistence.getStore());
        assertEquals(tokenPersistence.getStore().size(), 1);
        assertNotNull(authenticationService.getUser().getUserId());
        assertEquals(authenticationService.getUser().getUserId().id(), userPersistence.next().id());
        assertEquals(authenticationService.getUser().getPassword(), password);
        assertEquals(output.token(), "signed-jwt-token");
        assertEquals(output.refreshToken(), "refresh-signed-jwt-token");
        assertEquals(output.role(), RoleConstant.USER);
    }
}
