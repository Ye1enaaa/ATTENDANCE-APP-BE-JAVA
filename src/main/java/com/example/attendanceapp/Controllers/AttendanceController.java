package com.example.attendanceapp.Controllers;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.attendanceapp.Models.Attendance;
import com.example.attendanceapp.Models.User;
import com.example.attendanceapp.Repository.AttendanceRepository;
import com.example.attendanceapp.Repository.UserRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping(path = "/api")
public class AttendanceController {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping(path = "/create-attendance/")
    public @ResponseBody Object createAttendance(@RequestParam Integer employeeId, @RequestBody Attendance attendanceBody) {
       Attendance attendance = new Attendance();

       Optional<User> optionalUser = userRepository.findByEmployeeId(employeeId);

       if(!optionalUser.isPresent()){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
       }

       User user = optionalUser.get();
       attendance.setName(attendanceBody.getName());
       attendance.setTimeIn(attendanceBody.getTimeIn());
       attendance.setTimeOut(attendanceBody.getTimeOut());
       attendance.setUser(user);
       attendanceRepository.save(attendance);

       HashMap<String, String> map = new HashMap<>();
       map.put("message", "Attendance Added Successfully");
       return map;
    }
    
}
