package com.kafka.reactive.kafka.reactive.token;

import com.kafka.reactive.kafka.reactive.adapter.rest.dto.UserLogin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.crypto.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expirationTime;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserLogin userLogin) {
        final Map<String, Object> claims = Map.of(
            "role", List.of(userLogin.getRole()),
            "permissions", userLogin.getPermissions());
        return createToken(claims, userLogin.getUsername());
    }

    public String getUsername(String authToken) {
        return getClaimsFromToken(authToken).getSubject();
    }

    public boolean validateExpiration(String authToken) {
        final Date expiration = getClaimsFromToken(authToken).getExpiration();
        return expiration.after(new Date());
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private String createToken(Map<String, Object> claims, String username) {
        final Long expirationTimeLong = Long.parseLong(expirationTime);
        final Date createdAt = new Date();
        final Date expiresAt = new Date(createdAt.getTime() + expirationTimeLong);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(createdAt)
            .setExpiration(expiresAt)
            .signWith(key)
            .compact();
    }
}
