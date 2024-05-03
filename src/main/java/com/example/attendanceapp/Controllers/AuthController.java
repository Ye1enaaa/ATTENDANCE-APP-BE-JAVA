package com.example.attendanceapp.Controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.attendanceapp.Models.User;
import com.example.attendanceapp.Repository.UserRepository;
import com.example.attendanceapp.Security.JwtIssuer;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtIssuer jwtIssuer;

    @PostMapping(path = "/auth/login")
    public @ResponseBody ResponseEntity<?> logIn(@RequestBody User credentials, HttpServletResponse response) {
        Optional<User> findUsingEmail = userRepository.findByEmail(credentials.getEmail());
        var accessToken = jwtIssuer.issue(1, credentials.getEmail());
        try {
            if (!findUsingEmail.isPresent() || credentials.getEmail().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Credentials on Database");
            }

            User user = findUsingEmail.get();
            boolean result = passwordEncoder.matches(credentials.getPassword(), user.getPassword());

            if (result) {
                Cookie cookie = new Cookie("accessToken", accessToken);
                cookie.setMaxAge(7 * 24 * 60 * 60);
                cookie.setPath("/");
                cookie.setSecure(true);
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
                HashMap<String, Object> responseDatas = new HashMap<>();
                Map<String, String> responseData = new HashMap<>();
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
