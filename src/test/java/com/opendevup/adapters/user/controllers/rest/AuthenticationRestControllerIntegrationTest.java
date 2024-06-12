package com.opendevup.adapters.user.controllers.rest;

import com.opendevup.adapters.shared.controllers.rest.AbstractControllerTest;
import com.opendevup.adapters.user.controllers.dto.SignInUserDto;
import com.opendevup.adapters.user.controllers.dto.SignUpUserDto;
import com.opendevup.core.user.models.RoleConstant;
import com.opendevup.core.user.usecases.SignInUserUseCase;
import com.opendevup.core.user.usecases.SignUpUserUseCase;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationRestControllerIntegrationTest extends AbstractControllerTest {
    @MockBean
    private SignUpUserUseCase signUpUserUseCaseMock;
    @MockBean
    private SignInUserUseCase signInUserUseCaseMock;

    private final static String PATH_SIGNUP = "/register";
    private final static String PATH_SIGNIN = "/token";

    @Test
    public void shouldCanCreateUser() throws Exception {
        String email = "user@test.ts";
        var dto = new SignUpUserDto("John", "Doe", email, "userPassword");
        var json = mapToJson(dto);

        String fakeUuid = "608fb1d0-23be-4885-a0e7-b02e3c8c796f";
        when(signUpUserUseCaseMock.apply(
                new SignUpUserUseCase.Input(
                        dto.firstName(),
                        dto.lastName(),
                        dto.email(),
                        dto.password()
                )
        )).thenReturn(new SignUpUserUseCase.Output(fakeUuid));

        mvc.perform(
                        post(PATH_SIGNUP)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Is.is(fakeUuid)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldCanGetAccessToken() throws Exception {
        String email = "user@test.ts";
        var dto = new SignInUserDto(email, "very-strong-password-123@&/");
        var json = mapToJson(dto);

        String token = "signed-jwt-token";
        String refreshToken = "refresh-signed-jwt-token";
        String role = RoleConstant.USER;
        String fullName = "John Doe";

        when(signInUserUseCaseMock.apply(
                new SignInUserUseCase.Input(
                        dto.username(),
                        dto.password()
                )
        )).thenReturn(new SignInUserUseCase.Output(token, refreshToken, fullName, role));

        mvc.perform(
                        post(PATH_SIGNIN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", Is.is(token)))
                .andExpect(jsonPath("$.refreshToken", Is.is(refreshToken)))
                .andExpect(jsonPath("$.fullName", Is.is(fullName)))
                .andExpect(jsonPath("$.role", Is.is(role)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}