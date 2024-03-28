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
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping(path = "/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    PasswordEncoder passwordEncoder;

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

            HashMap<String, String> map = new HashMap<>();
            map.put("message", "User Added Successfully");
            return map;
        }
    }
    @GetMapping(path = "/users")
    public @ResponseBody Object getAllUser() {
        Iterable <User> users = userRepository.findAll();
        return users;
    }
    
    
}
