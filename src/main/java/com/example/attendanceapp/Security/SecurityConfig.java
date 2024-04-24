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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     // return http.authorizeHttpRequests(
    //     //     request -> request.requestMatchers(
    //     //         new AntPathRequestMatcher("/auth/login"))
    //     //         .permitAll())
    //     //         .authorizeHttpRequests(request -> request.requestMatchers(new
    //     //         AntPathRequestMatcher("/admin/add/user"))
    //     //         .authenticated())
    //     //         .httpBasic(Customizer.withDefaults())
    //     //         .build();
    //     http
    //         // .csrf(AbstractHttpConfigurer::disable)
    //         .authorizeHttpRequests((request) -> request
    //             .requestMatchers("/admin/add/user", "/register", "/home", "/hello").permitAll()
    //             .requestMatchers("/rest/auth/welcome").permitAll()
    //             // .requestMatchers(HttpMethod.POST, "/auth/u/signup").permitAll()
    //             .requestMatchers(HttpMethod.GET).hasAuthority("read")
    //             .requestMatchers(HttpMethod.POST).hasAuthority("write")
    //             .anyRequest().authenticated())
    //         // .formLogin((form) -> form
    //         //     .loginPage("/auth/login")
    //         //     .permitAll()
    //         //     .defaultSuccessUrl("/hello", true)) // Redirect to /hello after successful login)
    //         //.httpBasic(AbstractHttpConfigurer::disable)
    //         .logout((logout) -> logout.permitAll())
    //         .sessionManagement((sessionManagement) ->
    //              sessionManagement
    //                  .sessionConcurrency((sessionConcurrency) ->
    //                      sessionConcurrency
    //                          .maximumSessions(100)
    //                          .expiredUrl("/login?expired")
    //                  )
    //                  //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //          );

 //       return http.build();
 //   }

    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
    //     http.authorizeHttpRequests
    //     (authorize -> 
    //     //authorize.anyRequest().permitAll() 
    //     authorize.requestMatchers("/api").permitAll()
    //     .requestMatchers("/admin/add/user").authenticated()
    //     )
    //     .formLogin(formLogin -> formLogin.loginPage("/auth/login"));
    //     return http.build();
    //     // and().authorize.requestMatchers("/api/user").authenticated()
    // }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
    //     http.authorizeHttpRequests(
    //         //.requestMatchers("/api").permitAll()
    //         authorize -> authorize.requestMatchers("/api/add/user").authenticated())
    //         .formLogin(
    //             formLogin -> formLogin.loginPage("/auth/login")
    //             .permitAll()
    //         ).rememberMe(Customizer.withDefaults());
    //     return http.build();
    // }
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // http.
        // csrf(AbstractHttpConfigurer::disable)
        // .authorizeHttpRequests((request)-> 
        // request.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
        // .requestMatchers(HttpMethod.POST, "/admin/add/user").authenticated()
        // )
        
        // .formLogin(login -> login.loginPage("/auth/login").permitAll()
        // );

        http.
        csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests((request)-> request
            .requestMatchers("/users").permitAll()
            .requestMatchers(HttpMethod.POST, "/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/create-attendance/*").permitAll()
            //.requestMatchers(HttpMethod.POST, "/create-attendance/*").permitAll()
            //.requestMatchers(HttpMethod.POST, "/login").permitAll()
            .requestMatchers(HttpMethod.GET).hasAuthority("read")
            .requestMatchers(HttpMethod.POST).hasAuthority("write")
            .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
    // SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    //     http.authorizeHttpRequests(
    //         (request)-> request.requestMatchers("/auth/login").permitAll()
    //         .requestMatchers("/admin/add/user").authenticated()
    //         )
            
    //         .formLogin(login -> login.loginPage("/auth/login").permitAll()
    //         .defaultSuccessUrl("/admin/add/user")
    //         )
    //         .logout(logout -> logout.logoutUrl("/logout"))
    //         ;

    //     return http.build();
    // }
}