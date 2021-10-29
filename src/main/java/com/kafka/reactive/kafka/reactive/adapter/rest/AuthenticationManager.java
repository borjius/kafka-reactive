package com.kafka.reactive.kafka.reactive.adapter.rest;

import com.kafka.reactive.kafka.reactive.token.TokenService;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenService tokenService;

    @Autowired
    public AuthenticationManager(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        final String authToken = authentication.getCredentials().toString();
        final String username = tokenService.getUsername(authToken);
        return Mono.just(tokenService.validateExpiration(authToken))
            .filter(valid -> valid)
            .switchIfEmpty(Mono.empty())
            .map(valid -> {
                final Claims claims = tokenService.getClaimsFromToken(authToken);
                final List<String> rolesMap = claims.get("role", List.class);
                return new UsernamePasswordAuthenticationToken(username,
                    null,
                    rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            });
    }
}
