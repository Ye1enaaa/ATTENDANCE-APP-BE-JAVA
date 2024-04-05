package com.example.attendanceapp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendanceapp.Models.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
    //Optional<Attendance> findByEmployeeId(Integer employeeId);
    List<Attendance> findByEmployeeIdOrderByDaTeDesc(Integer employeeId);
}