package com.example.attendanceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AttendanceappApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceappApplication.class, args);
	}

}
