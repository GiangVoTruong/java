package com.java.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "jyBCqutrnnxP11RQYuom3QAWZ1p5ddfN5IvWc6n15kI=";

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, String email, String phone) {
        return buildToken(username, email, phone, 1000 * 60 * 60 * 24);
    }

    public String generateRefreshToken(String username, String email, String phone) {
        return buildToken(username, email, phone, 1000L * 60 * 60 * 24 * 7);
    }

    private String buildToken(String username, String email, String phone, long expirationMs) {
        return Jwts.builder()
                .subject(username)
                .claim("email", email)
                .claim("phone", phone)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public String extractPhone(String token) {
        return extractAllClaims(token).get("phone", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            return extractUsername(token) != null && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
