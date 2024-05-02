package com.example.attendanceapp.Controllers;

//import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.attendanceapp.Models.User;
//import com.example.attendanceapp.Providers.JwtTokenProvider;
import com.example.attendanceapp.Repository.UserRepository;
//import com.example.attendanceapp.UTILS.JwtUtils;
import com.example.attendanceapp.Security.JwtIssuer;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

//import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {
    @Autowired
    private JwtIssuer jwtIssuer;
    
    @Autowired
    private UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    
    //private SecretKey secretKey = JwtUtils.generateSecretKey();

    @PostMapping(path = "/add/user")
    public @ResponseBody Object addUser(@RequestBody User userBody) {
        User user = new User();
        this.passwordEncoder = new BCryptPasswordEncoder();
        String encodedPasswd = passwordEncoder.encode(userBody.getPassword());
        Optional<User> findEmail = userRepository.findByEmail(userBody.getEmail());
        Optional<User> findEmpId = userRepository.findByEmployeeId(userBody.getEmployeeId());

        if (findEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email Already Exists");
        }
        else if(findEmpId.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee ID Number Already Exists");
        }
        else if(userBody.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must have 8 minimum characters");
        }
        else {
            user.setName(userBody.getName());
            user.setEmail(userBody.getEmail());
            user.setAddress(userBody.getAddress());
            user.setEmployeeId(userBody.getEmployeeId());
            user.setPassword(encodedPasswd);
            userRepository.save(user);

            HashMap<String, Object> responseData = new HashMap<>();
            HashMap<String, String> map = new HashMap<>();
            map.put("message", "User Added Successfully");
            map.put("name", user.getName());
            map.put("email", user.getEmail());
            map.put("address", user.getAddress());
            map.put("employeeId", user.getEmployeeId().toString());
            responseData.put("data", map);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }
    }
    @GetMapping(path = "/users")
    public @ResponseBody Object getAllUser() {
        Iterable <User> users = userRepository.findAll();
        return users;
    }

   
    @GetMapping(path="/user")
    public @ResponseBody Object getMyCredentials(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token == null) {
            System.out.println("No token"); 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided");
        }

        try {
            DecodedJWT decodedJWT = jwtIssuer.validateAndDecode(token);
            System.out.println(decodedJWT.getPayload()); 
            String userId = decodedJWT.getSubject();
            String email = decodedJWT.getClaim("email").asString();
            return ResponseEntity.status(HttpStatus.OK).body("User ID: " + userId + ", Email: " + email);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
    

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); 
        }
        return null;
    }
   
}
