package com.example.attendanceapp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendanceapp.Models.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
    List<Attendance> findByEmployeeIdOrderByDaTeDesc(Integer employeeId);
}