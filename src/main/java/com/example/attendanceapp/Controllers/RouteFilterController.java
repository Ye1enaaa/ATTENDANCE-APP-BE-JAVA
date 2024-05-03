package com.example.attendanceapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RouteFilterController {
    
    @Autowired
    private UserController userController;

    @GetMapping(path="/**")
    public @ResponseBody Object handleRequests(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/user/")) {
            String token = userController.resolveToken(request);
            if (!token.isEmpty() && requestURI.startsWith("/user/credentials")) {
                System.out.println(token); 
                return userController.getMyCredentials(request);
            }
            return null;
        } else {
            return null;
        }
    }
}
