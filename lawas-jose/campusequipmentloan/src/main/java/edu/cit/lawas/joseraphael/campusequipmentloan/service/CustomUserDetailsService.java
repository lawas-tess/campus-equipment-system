package edu.cit.lawas.joseraphael.campusequipmentloan.service;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.StudentEntity;
import edu.cit.lawas.joseraphael.campusequipmentloan.repository.StudentRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    public CustomUserDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StudentEntity student = studentRepository.findByUname(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(student.getUname())
                .password(student.getPassword())
                .roles("STUDENT")
                .build();
    }
}
