package com.opendevup.adapters.user.controllers.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SignInUserDto(
        @NotNull(message = ErrorMessageValidationRequest.USERNAME_IS_REQUIRED)
        @NotEmpty(message = ErrorMessageValidationRequest.USERNAME_IS_REQUIRED)
        String username,
        @NotNull(message = ErrorMessageValidationRequest.PASSWORD_IS_REQUIRED)
        @NotEmpty(message = ErrorMessageValidationRequest.PASSWORD_IS_REQUIRED)
        String password
) {
}
