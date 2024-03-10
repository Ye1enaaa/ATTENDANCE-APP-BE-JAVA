package com.example.attendanceapp.Repository;

import org.springframework.data.repository.CrudRepository;

import com.example.attendanceapp.Models.User;

public interface UserRepository extends CrudRepository<User, Integer>{
    
}
