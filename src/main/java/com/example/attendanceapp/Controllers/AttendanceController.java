package com.example.attendanceapp.Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping(path = "/api")
public class AttendanceController {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping(path = "/create-attendance/{employeeId}")
    public @ResponseBody Object createAttendance(@PathVariable Integer employeeId, @RequestBody Attendance attendanceBody) {
       Attendance attendance = new Attendance();

       Optional<User> optionalUser = userRepository.findByEmployeeId(employeeId);
       //Optional<Attendance> attendanceValue = attendanceRepository.findByEmployeeId(employeeId);
       User user = optionalUser.get();
       //Attendance attendanceRow = attendanceValue.get();
       LocalTime localTimeNow = LocalTime.now(ZoneId.of("GMT+08:00"));
       LocalDate localDateNow = LocalDate.now(ZoneId.of("GMT+08:00"));
       

       if(localTimeNow.isAfter(LocalTime.MIDNIGHT) && localTimeNow.isBefore(LocalTime.NOON)){
        if(!optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
           }
    
           attendance.setName(user.getName());
           attendance.setTimeIn(localTimeNow.toString());
           attendance.setDate(localDateNow.toString());
           attendance.setEmployeeId(employeeId);
           attendance.setUser(user);
           attendanceRepository.save(attendance);

           HashMap<String, Object> responseData = new HashMap<>();
           HashMap<String, String> data = new HashMap<>();
           data.put("name", attendance.getName());
           data.put("employeeId", attendance.getEmployeeId().toString());
           data.put("timeIn", attendance.getTimeIn());
           data.put("userId", user.getId().toString());
           responseData.put("data", data);
           return ResponseEntity.ok(responseData);
       }else{
        // HashMap<String, Object> responseData = new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        data.put("message", "Time In can only be between 12:00am MIDNIGHT to 11:59am NOON");
        //responseData.put("message", optionalUser)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
       }
    }

    @PutMapping(path = "/time-out/{employeeId}")
    public @ResponseBody Object putTimeOut(@PathVariable Integer employeeId, @RequestBody Attendance attendanceBody) {
        Attendance attendance = new Attendance();
        LocalTime localTimeNow = LocalTime.now(ZoneId.of("GMT+08:00"));
        LocalDate localDateNow = LocalDate.now(ZoneId.of("GMT+08:00"));
        Optional<Attendance> findUserByEmpId = attendanceRepository.findByEmployeeId(employeeId);
        Attendance attendanceRow = findUserByEmpId.get();

        if(attendanceRow.getDate().equals(localDateNow.toString())){
            attendance.setTimeOut(localTimeNow.toString());
            attendanceRepository.save(attendance);
        }
        HashMap<String, Object> responseData = new HashMap<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("name", attendanceRow.getName());
        map.put("timeIn", attendanceRow.getTimeIn());
        map.put("timeOut", attendance.getTimeOut());
        responseData.put("data", map);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
        
    }
    
}
