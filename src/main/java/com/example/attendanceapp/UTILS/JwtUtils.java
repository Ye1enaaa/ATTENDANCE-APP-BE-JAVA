package com.example.attendanceapp.UTILS;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@SuppressWarnings("deprecation")
public class JwtUtils {
    public static SecretKey generateSecretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}
