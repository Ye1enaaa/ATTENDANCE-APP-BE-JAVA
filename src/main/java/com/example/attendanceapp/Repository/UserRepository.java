package com.example.attendanceapp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendanceapp.Models.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByEmployeeId(Integer employeeId);
    Optional<User> findByEmail(String email);
}
