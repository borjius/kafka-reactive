package com.kafka.reactive.kafka.reactive.adapter.rest;

import com.kafka.reactive.kafka.reactive.adapter.rest.dto.UserLogin;
import com.kafka.reactive.kafka.reactive.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class LoginController {

    private final TokenService tokenService;

    @Autowired
    public LoginController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public Mono<String> login(@RequestBody Mono<UserLogin> user) {
        // a service to check correct user/pass is discarded for this poc
        return user.map(userLogin -> tokenService.generateToken(userLogin));
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public Mono<String> assertToken() {
        return Mono.just("OK!");
    }

}
