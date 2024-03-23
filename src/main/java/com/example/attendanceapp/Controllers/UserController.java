package com.example.attendanceapp.Controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.attendanceapp.Models.User;
import com.example.attendanceapp.Repository.UserRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;


@Controller
@RequestMapping(path = "/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add/user")
    public @ResponseBody Object addUser(@RequestBody User userBody) {
        User user = new User();
        

        user.setName(userBody.getName());
        user.setEmail(userBody.getEmail());
        user.setAddress(userBody.getAddress());
        user.setPassword(userBody.getPassword());
        userRepository.save(user);

        HashMap<String, String> map = new HashMap<>();
        map.put("message", "User Added Successfully");
        return map;
    }
    
}