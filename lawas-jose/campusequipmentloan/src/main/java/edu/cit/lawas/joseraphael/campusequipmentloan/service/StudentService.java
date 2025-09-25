package edu.cit.lawas.joseraphael.campusequipmentloan.service;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.StudentEntity;
import edu.cit.lawas.joseraphael.campusequipmentloan.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public StudentEntity register(StudentEntity student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

    public StudentEntity getById(Long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    public List<StudentEntity> getAll() {
        return studentRepository.findAll();
    }

    public void save(StudentEntity student) {
        if (studentRepository.findByStudentNo(student.getStudentNo()) != null) {
            throw new IllegalStateException("Student already exists with student number: " + student.getStudentNo());
        }
        studentRepository.save(student);
    }

    public StudentEntity getByStudentNo(String studentNo) {
        return studentRepository.findByStudentNo(studentNo);
    }
}
