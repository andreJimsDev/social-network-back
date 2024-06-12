package com.opendevup.core.user.usecases;

import com.opendevup.adapters.user.gateways.hash.DeterministicHashGenerator;
import com.opendevup.adapters.user.gateways.persistence.fake.InMemoryUserPersistence;
import com.opendevup.core.shared.model.Email;
import com.opendevup.core.shared.model.exceptions.EntityAlreadyExistException;
import com.opendevup.core.shared.model.exceptions.EntityAlreadyExistMessage;
import com.opendevup.core.shared.model.exceptions.ValidationInputException;
import com.opendevup.core.shared.model.exceptions.ValidationInputMessage;
import com.opendevup.core.user.models.Role;
import com.opendevup.core.user.models.RoleConstant;
import com.opendevup.core.user.models.User;
import com.opendevup.core.user.models.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignUpUserUseCaseTest {
    private InMemoryUserPersistence userPersistence;
    private SignUpUserUseCase useCase;

    @BeforeEach
    public void setup() {
        DeterministicHashGenerator hashGenerator = new DeterministicHashGenerator("hashed");
        userPersistence = new InMemoryUserPersistence();
        useCase = new SignUpUserUseCase(
                userPersistence,
                hashGenerator
        );
    }

    @Test
    public void shouldCannotCreateAccountWithoutFirstName() {
        var input = new SignUpUserUseCase.Input(
                null,
                "Doe",
                "user@test.ts",
                "userTestPassword"
        );

        ValidationInputException exception = assertThrows(ValidationInputException.class, () -> useCase.apply(input));

        assertEquals(ValidationInputMessage.FIRST_NAME_IS_EMPTY, exception.getMessage());
    }

    @Test
    public void shouldCannotCreateAccountWithoutLastName() {
        var input = new SignUpUserUseCase.Input(
                "John",
                null,
                "user@test.ts",
                "userTestPassword"
        );
        ValidationInputException exception = assertThrows(ValidationInputException.class, () -> useCase.apply(input));

        assertEquals(ValidationInputMessage.LAST_NAME_IS_EMPTY, exception.getMessage());
    }

    @Test
    public void shouldCannotCreateAccountWithoutPassword() {
        var input = new SignUpUserUseCase.Input(
                "John",
                "Doe",
                "user@test.ts",
                ""
        );
        ValidationInputException exception = assertThrows(ValidationInputException.class, () -> useCase.apply(input));

        assertEquals(ValidationInputMessage.PASSWORD_IS_EMPTY, exception.getMessage());
    }

    @Test
    public void shouldCannotCreateAccountWithInvalidEmail() {
        var input = new SignUpUserUseCase.Input(
                "John",
                "Doe",
                "user@test",
                "userTestPassword"
        );
        ValidationInputException exception = assertThrows(ValidationInputException.class, () -> useCase.apply(input));

        assertEquals(ValidationInputMessage.EMAIL_IS_INVALID, exception.getMessage());
    }

    @Test
    public void shouldCannotCreateAccountWhenEmailAlreadyExists() {
        var input = new SignUpUserUseCase.Input(
                "John",
                "Doe",
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

        EntityAlreadyExistException exception = assertThrows(EntityAlreadyExistException.class, () -> useCase.apply(input));

        assertEquals(EntityAlreadyExistMessage.USER_IS_ALREADY_EXIST, exception.getMessage());
    }

    @Test
    public void shouldUserIsPersistedWhenCreateUserIsSuccessful() {
        String email = "user@test.ts";
        var input = new SignUpUserUseCase.Input(
                "John",
                "Doe",
                email,
                "userTestPassword"
        );

        useCase.apply(input);

        assertTrue(userPersistence.getStore().containsValue(expectedUser()));
    }


    private User expectedUser(){
        return User.builder()
                .userId(userPersistence.next())
                .firstName("John")
                .lastName("Doe")
                .email(Email.of("user@test.ts"))
                .password("hashed:my-strong-password")
                .role(Role.of(RoleConstant.USER))
                .build();
    }
}
