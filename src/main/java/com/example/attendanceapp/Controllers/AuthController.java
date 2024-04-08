package com.example.attendanceapp.Controllers;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.attendanceapp.Models.User;
import com.example.attendanceapp.Repository.UserRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping(path = "/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    PasswordEncoder passwordEncoder;
    @PostMapping(path = "/login")
    public @ResponseBody Object logIn(@RequestBody User credentials) {
        
        Optional<User> findUsingEmail = userRepository.findByEmail(credentials.getEmail());
        User user = findUsingEmail.get();
        this.passwordEncoder = new BCryptPasswordEncoder();
        boolean result = passwordEncoder.matches(credentials.getPassword(), user.getPassword());
        int iterate = 0;
        try {
            if(!findUsingEmail.isPresent()){
                HashMap<String, String> responseData = new HashMap<>();
                responseData.put("msg", "No Credentials on Database");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
            
            if(user.getEmail().equals(credentials.getEmail()) && result){
                iterate = 0;
                return ResponseEntity.status(HttpStatus.OK).body("Login Successful");
            }
            else{
                iterate++;
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(iterate);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
     
}
