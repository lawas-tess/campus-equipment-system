package edu.cit.lawas.joseraphael.campusequipmentloan.config;

import edu.cit.lawas.joseraphael.campusequipmentloan.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public WebConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // Password encoder for both student + admin
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // In-memory admin (values can come from application.properties)
    @Bean
    public InMemoryUserDetailsManager adminUserDetailsService(
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.username:admin}") String username,
            @Value("${app.admin.password:admin123}") String password) {

        return new InMemoryUserDetailsManager(
                User.withUsername(username)
                        .password(passwordEncoder.encode(password))
                        .roles("ADMIN")
                        .build()
        );
    }

    // AuthenticationManager combines Student DB + Admin in-memory
    @Bean
    public AuthenticationManager authManager(HttpSecurity http,
                                             PasswordEncoder passwordEncoder,
                                             InMemoryUserDetailsManager adminUserDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder) // students
                .and()
                .userDetailsService(adminUserDetailsService).passwordEncoder(passwordEncoder) // admin
                .and()
                .build();
    }

    // Security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/students/register").permitAll() // open for registration
                        .requestMatchers("/admin/**").hasRole("ADMIN") // admin pages
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}
