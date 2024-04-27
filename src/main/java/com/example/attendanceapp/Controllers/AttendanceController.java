package com.example.attendanceapp.Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
public class AttendanceController {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping(path = "/create-attendance/{employeeId}")
    public @ResponseBody Object createAttendance(@PathVariable Integer employeeId, @RequestBody Attendance attendanceBody) {
       Attendance attendance = new Attendance();

       Optional<User> optionalUser = userRepository.findByEmployeeId(employeeId);
       List<Attendance> attendanceValue = attendanceRepository.findByEmployeeIdOrderByDaTeDesc(employeeId);
       User user = optionalUser.get();
       List<Attendance> attendanceRow = attendanceValue;
       LocalTime localTimeNow = LocalTime.now(ZoneId.of("GMT+08:00"));
       LocalDate localDateNow = LocalDate.now(ZoneId.of("GMT+08:00"));
       

       if(localTimeNow.isAfter(LocalTime.MIDNIGHT) && localTimeNow.isBefore(LocalTime.NOON)){
        if(!optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
           }

        if(attendanceRow.getFirst().getDate().equals(localDateNow.toString())){
            HashMap<String, Object> responseData = new HashMap<>();
            HashMap<String, String> data = new HashMap<>();
            data.put("msg", "You already have attendance this day");
            data.put("val", attendanceRow.getFirst().getDate());
            responseData.put("data", data);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
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
        //Attendance attendance = new Attendance();
        LocalTime localTimeNow = LocalTime.now(ZoneId.of("GMT+08:00"));
        LocalDate localDateNow = LocalDate.now(ZoneId.of("GMT+08:00"));
        List<Attendance> findUserByEmpId = attendanceRepository.findByEmployeeIdOrderByDaTeDesc(employeeId);
        List<Attendance> attendanceRow = findUserByEmpId;

        try {
            if(!attendanceRow.isEmpty()){
                if(attendanceRow.getFirst().getDate().equals(localDateNow.toString())){
                    attendanceRow.getFirst().setTimeOut(localTimeNow.toString());
                    attendanceRepository.save(attendanceRow.getFirst());
                    HashMap<String, Object> responseData = new HashMap<>();
                    HashMap<String, String> data = new HashMap<>();
                    data.put("data", "Attendance Time Out Success");
                    responseData.put("data", data);
                    return ResponseEntity.status(HttpStatus.OK).body(responseData);
                }
                else{
                    HashMap<String, Object> responseData = new HashMap<>();
                    HashMap<String, String> data = new HashMap<>();
                    data.put("msg", "You already have time out for today");
                    responseData.put("data", data);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
                }
            }
            else{
                HashMap<String, Object> responseData = new HashMap<>();
                HashMap<String, String> data = new HashMap<>();
                data.put("msg", "No Attendance for Today");
                responseData.put("data", data);
                return ResponseEntity.status(HttpStatus.OK).body(responseData);
            }
        } 
        catch(DataAccessException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
        
    }
    
}
