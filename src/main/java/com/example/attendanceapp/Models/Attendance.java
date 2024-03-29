package com.example.attendanceapp.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long attendanceId;
    private String name;
    private String timeIn;
    private String timeOut;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getAttendanceId(){
        return attendanceId;
    }

    public void setAttendanceId(Long attendanceId){
        this.attendanceId = attendanceId;
    } 

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getTimeIn(){
        return timeIn;
    }

    public void setTimeIn(String timeIn){
        this.timeIn = timeIn;
    }

    public String getTimeOut(){
        return timeOut;
    }

    public void setTimeOut(String timeOut){
        this.timeOut = timeOut;
    }

    public User getUser(){
        return user;
    }
    
    public void setUser(User user){
        this.user = user;
    }

}
