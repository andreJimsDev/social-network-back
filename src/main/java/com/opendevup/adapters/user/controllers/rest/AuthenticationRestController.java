package com.opendevup.adapters.user.controllers.rest;

import com.opendevup.adapters.user.controllers.dto.SignInUserDto;
import com.opendevup.adapters.user.controllers.dto.SignUpUserDto;
import com.opendevup.core.user.usecases.RefreshTokenUseCase;
import com.opendevup.core.user.usecases.SignInUserUseCase;
import com.opendevup.core.user.usecases.SignUpUserUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationRestController {
    private final SignInUserUseCase signInUserUseCase;
    private final SignUpUserUseCase signUpUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/token")
    public ResponseEntity<SignInUserUseCase.Output> signIn(@Validated @RequestBody SignInUserDto dto) {
        return ResponseEntity.ok(signInUserUseCase.apply(
                new SignInUserUseCase.Input(
                        dto.username(),
                        dto.password()
                )
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<SignUpUserUseCase.Output> signUp(@Validated @RequestBody SignUpUserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpUserUseCase.apply(
                new SignUpUserUseCase.Input(
                        dto.firstName(),
                        dto.lastName(),
                        dto.email(),
                        dto.password()
                )
        ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenUseCase.Output> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return ResponseEntity.ok(refreshTokenUseCase.apply(
                new RefreshTokenUseCase.Input(authHeader)
        ));
    }
}