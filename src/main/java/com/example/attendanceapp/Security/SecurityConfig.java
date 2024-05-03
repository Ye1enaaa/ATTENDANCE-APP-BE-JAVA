package com.example.attendanceapp.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.attendanceapp.Repository.UserRepository;

@Configuration
@EnableWebSecurity

public class SecurityConfig{
    @Autowired
        private UserRepository userRepository;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
            http.
            csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((request)-> request
                .requestMatchers("/**").permitAll()
                //.requestMatchers("/user/**").permitAll()
                //.requestMatchers("/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/create-attendance/*").permitAll()
                .requestMatchers(HttpMethod.PUT, "/time-out/*").permitAll()
                .requestMatchers(HttpMethod.GET).hasAuthority("read")
                .requestMatchers(HttpMethod.POST).hasAuthority("write")
                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }  
}