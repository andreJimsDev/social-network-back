package com.opendevup.core.user.usecases;

import com.opendevup.core.shared.model.Email;
import com.opendevup.core.shared.model.exceptions.EntityAlreadyExistException;
import com.opendevup.core.shared.model.exceptions.EntityAlreadyExistMessage;
import com.opendevup.core.shared.model.exceptions.ValidationInputException;
import com.opendevup.core.shared.model.exceptions.ValidationInputMessage;
import com.opendevup.core.user.gateways.HashGenerator;
import com.opendevup.core.user.models.Role;
import com.opendevup.core.user.models.RoleConstant;
import com.opendevup.core.user.models.User;
import com.opendevup.core.user.gateways.UserPersistenceGateway;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class SignUpUserUseCase implements Function<SignUpUserUseCase.Input, SignUpUserUseCase.Output> {

    private final UserPersistenceGateway userPersistenceGateway;
    private final HashGenerator hashGenerator;

    @Override
    public Output apply(Input input) {
        if (StringUtils.isEmpty(input.lastName)) {
            throw new ValidationInputException(ValidationInputMessage.LAST_NAME_IS_EMPTY);
        }

        if (StringUtils.isEmpty(input.firstName)) {
            throw new ValidationInputException(ValidationInputMessage.FIRST_NAME_IS_EMPTY);
        }

        if (StringUtils.isEmpty(input.password)) {
            throw new ValidationInputException(ValidationInputMessage.PASSWORD_IS_EMPTY);
        }

        Email email = Email.of(input.email);
        Optional<User> userOptional = userPersistenceGateway.findByEmail(email);

        if (userOptional.isPresent())
            throw new EntityAlreadyExistException(EntityAlreadyExistMessage.USER_IS_ALREADY_EXIST);

        User user = User.builder()
                .userId(userPersistenceGateway.next())
                .firstName(input.firstName())
                .lastName(input.lastName())
                .email(email)
                .password(hashGenerator.generate(input.password()))
                .role(Role.of(RoleConstant.USER))
                .build();

        userPersistenceGateway.add(user);

        return new Output(user.getUserId().id());
    }

    public record Input(String firstName,
                        String lastName,
                        String email,
                        String password) {
    }

    public record Output(String id) {
    }
}
