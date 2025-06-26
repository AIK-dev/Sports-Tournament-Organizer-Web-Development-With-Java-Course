package com.tournament_organizer.security;

import com.tournament_organizer.entity.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;
    private SecretKey key;
    @PostConstruct
    private void init() {
        byte[] bytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }
    public String generateToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("roles", user.getRole())
                .issuedAt(now)
                .expiration(Date.from(now.toInstant().plusMillis(jwtExpirationMs)))
                .signWith(key)
                .compact();
    }
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public boolean validate(String token){
        try{
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (JwtException | IllegalArgumentException ex){
            return false;
        }
    }
}
