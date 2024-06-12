package com.opendevup.adapters.user.controllers.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SignUpUserDto(
        @NotNull(message = ErrorMessageValidationRequest.FIRST_NAME_IS_REQUIRED)
        @NotEmpty(message = ErrorMessageValidationRequest.FIRST_NAME_IS_REQUIRED)
        String firstName,
        @NotNull(message = ErrorMessageValidationRequest.LAST_NAME_IS_REQUIRED)
        @NotEmpty(message = ErrorMessageValidationRequest.LAST_NAME_IS_REQUIRED)
        String lastName,
        @NotNull(message = ErrorMessageValidationRequest.EMAIL_IS_REQUIRED)
        @NotEmpty(message = ErrorMessageValidationRequest.EMAIL_IS_REQUIRED)
        String email,
        @NotNull(message = ErrorMessageValidationRequest.PASSWORD_IS_REQUIRED)
        @NotEmpty(message = ErrorMessageValidationRequest.PASSWORD_IS_REQUIRED)
        String password

) {

}
