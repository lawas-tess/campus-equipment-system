package edu.cit.lawas.joseraphael.campusequipmentloan.controller;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.StudentEntity;
import edu.cit.lawas.joseraphael.campusequipmentloan.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Register new student
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody StudentEntity student) {
        try {
            StudentEntity saved = studentService.register(student);
            return ResponseEntity.ok(Map.of(
                    "message", "Student registered successfully.",
                    "id", saved.getId(),
                    "studentNo", saved.getStudentNo(),
                    "fname", saved.getFname(),
                    "lname", saved.getLname(),
                    "uname", saved.getUname(),
                    "email", saved.getEmail()
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    // Alternative addStudent (without password encoding)
    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody StudentEntity student) {
        try {
            studentService.save(student);
            return ResponseEntity.ok(Map.of(
                    "message", "Student added successfully.",
                    "id", student.getId(),
                    "studentNo", student.getStudentNo(),
                    "fname", student.getFname(),
                    "lname", student.getLname(),
                    "uname", student.getUname(),
                    "email", student.getEmail()
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    // Get all students
    @GetMapping
    public ResponseEntity<List<StudentEntity>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAll());
    }
}
