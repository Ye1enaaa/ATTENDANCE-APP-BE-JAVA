package com.example.attendanceapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendanceapp.Models.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    
}
