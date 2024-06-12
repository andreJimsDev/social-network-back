package com.opendevup.adapters;

import com.opendevup.core.user.usecases.SignInUserUseCase;
import com.opendevup.core.user.usecases.SignUpUserUseCase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitialize {

    private final SignUpUserUseCase signUpUserUseCase;
    private final SignInUserUseCase signInUserUseCase;

    @PostConstruct
    public void init() {
        log.info("BEGIN init data ....");
        signUpUserUseCase.apply(
                new SignUpUserUseCase.Input(
                        "Abbott",
                        "Keitch",
                        "userTest@yopmail.com",
                        "userTestPassword"
                )
        );

        signInUserUseCase.apply(
                new SignInUserUseCase.Input(
                        "userTest@yopmail.com",
                        "userTestPassword"
                )
        );

        log.info("END init data ....");
    }
}
