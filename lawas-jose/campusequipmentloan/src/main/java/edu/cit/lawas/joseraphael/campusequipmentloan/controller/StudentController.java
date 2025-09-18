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

    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody StudentEntity student) {
        try {
            studentService.save(student);
            return ResponseEntity.ok(Map.of(
                    "message", "Student added successfully.",
                    "id", student.getId(),
                    "studentNo", student.getStudentNo(),
                    "name", student.getName(),
                    "email", student.getEmail()
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(Map.of(
                    "error", e.getMessage(),
                    "studentNo", student.getStudentNo(),
                    "name", student.getName(),
                    "email", student.getEmail()
            ));
        }
    }



    @GetMapping
    public ResponseEntity<List<StudentEntity>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAll());
    }
}
