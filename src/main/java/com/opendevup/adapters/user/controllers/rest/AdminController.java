package com.opendevup.adapters.user.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Message get() {
        return new Message("GET:: admin controller");
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public String post() {
        return "POST:: admin controller";
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping
    public String put() {
        return "PUT:: admin controller";
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    public String delete() {
        return "DELETE:: admin controller";
    }

    public record Message(String message) {

    }
}