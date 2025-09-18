package edu.cit.lawas.joseraphael.campusequipmentloan.repository;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    @Query("SELECT COUNT(l) FROM LoanEntity l WHERE l.student.id = :studentId AND l.status IN ('ONGOING','OVERDUE')")
    long countActiveLoans(Long studentId);
}
