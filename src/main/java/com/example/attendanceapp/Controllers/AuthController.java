package com.example.attendanceapp.Controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.attendanceapp.Models.User;
//import com.example.attendanceapp.Providers.JwtTokenProvider;
import com.example.attendanceapp.Repository.UserRepository;
import com.example.attendanceapp.Security.JwtIssuer;
//import com.example.attendanceapp.UTILS.JwtUtils;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;

@SuppressWarnings("deprecation")
@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtIssuer jwtIssuer;
    //private SecretKey secretKey = JwtUtils.generateSecretKey();
    //private static final long JWT_EXPIRATION_MS = 86400000;

    @PostMapping(path = "/auth/login")
    public @ResponseBody ResponseEntity<?> logIn(@RequestBody User credentials) {
        Optional<User> findUsingEmail = userRepository.findByEmail(credentials.getEmail());
        var accessToken = jwtIssuer.issue(1, credentials.getEmail());
        try {
            if (!findUsingEmail.isPresent() || credentials.getEmail().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Credentials on Database");
            }

            User user = findUsingEmail.get();
            boolean result = passwordEncoder.matches(credentials.getPassword(), user.getPassword());

            if (result) {
                //String token = generateJwtToken(user.getEmail());
                HashMap<String, Object> responseDatas = new HashMap<>();
                Map<String, String> responseData = new HashMap<>();
                //responseData.put("secretKey", secretKey.toString());
                //responseData.put("token", token);
                responseData.put("accessToken", accessToken);
                responseDatas.put("values", responseData);
                return ResponseEntity.status(HttpStatus.OK).body(responseDatas);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
