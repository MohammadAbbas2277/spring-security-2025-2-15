package com.java24.spring_securitity_demo.util;

import com.java24.spring_securitity_demo.models.User;
import com.java24.spring_securitity_demo.services.UserDervice;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil{
    private final UserDervice userDervice;
    // secret key to generate tokens
    @Value("${jwt.secret}")
    private String jwtSecret;

    // how long token is valid (milli sek)
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public JwtUtil(UserDervice userDervice) {
        this.userDervice = userDervice;
    }

    // create encrypted key based on our secret values
    private Key getSigninKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

// validate token
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException | IllegalAccessError e){
            return false;
        }
    }


    // extract username from token
    public String extractUsername(String token) {
        return extractAllclaims(token).getSubject();
    }

    // check if token is eprired
    public boolean isTokenExpired(String token) {
        Date ecpiration = extractAllclaims(token).getExpiration();
        return ecpiration.before(new Date());
    }



    private Claims extractAllclaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }









}
