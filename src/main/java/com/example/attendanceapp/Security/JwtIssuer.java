package com.example.attendanceapp.Security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import ch.qos.logback.core.util.Duration;

@Component
public class JwtIssuer {
    private static final String SECRET = "secret";

    public DecodedJWT validateAndDecode(String token){
        try{
            return JWT.require(Algorithm.HMAC256(SECRET))
            .build().verify(token);
        }catch(Exception e){
            throw new RuntimeException("Invalid token");
        }
    }
    
    public String issue (Integer id, String email){
        return JWT.create()
            .withSubject(String.valueOf(id))
            .withClaim("email", email)
            .sign(Algorithm.HMAC256(SECRET));
    }
}
