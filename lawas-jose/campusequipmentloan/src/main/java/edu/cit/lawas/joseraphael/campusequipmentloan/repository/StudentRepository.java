package edu.cit.lawas.joseraphael.campusequipmentloan.repository;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    StudentEntity findByStudentNo(String studentNo);
    Optional<StudentEntity> findByUname(String uname);
}
