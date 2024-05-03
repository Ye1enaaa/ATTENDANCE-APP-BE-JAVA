package com.example.attendanceapp.Providers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.attendanceapp.Models.User;
import com.example.attendanceapp.Repository.UserRepository;
import com.example.attendanceapp.Security.JwtIssuer;

@Component
public class PayloadProviders {
    @Autowired
    private JwtIssuer jwtIssuer;

    @Autowired
    private UserRepository userRepository;

    public Object userCredentialsPayloadProviders(String token){
        DecodedJWT decodedJWT = jwtIssuer.validateAndDecode(token);
        System.out.println(decodedJWT.getPayload()); 
        String email = decodedJWT.getClaim("email").asString();
        Optional<User> findByEmailOptional = userRepository.findByEmail(email);
        
        if (findByEmailOptional.isPresent()) {
            return findByEmailOptional.get();
        } else {
            return null;
        }
    }
}
