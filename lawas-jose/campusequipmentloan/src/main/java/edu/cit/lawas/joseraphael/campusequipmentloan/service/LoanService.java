package edu.cit.lawas.joseraphael.campusequipmentloan.service;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.EquipmentEntity;
import edu.cit.lawas.joseraphael.campusequipmentloan.entity.LoanEntity;
import edu.cit.lawas.joseraphael.campusequipmentloan.entity.StudentEntity;
import edu.cit.lawas.joseraphael.campusequipmentloan.penalty.FixedPenaltyStrategy;
import edu.cit.lawas.joseraphael.campusequipmentloan.penalty.PenaltyStrategy;
import edu.cit.lawas.joseraphael.campusequipmentloan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final EquipmentService equipmentService;
    private final StudentService studentService;
    private final PenaltyStrategy penaltyStrategy;

    public LoanService(LoanRepository loanRepository,
                       EquipmentService equipmentService,
                       StudentService studentService) {
        this.loanRepository = loanRepository;
        this.equipmentService = equipmentService;
        this.studentService = studentService;
        this.penaltyStrategy = new FixedPenaltyStrategy();
    }

    public LoanEntity createLoan(Long studentId, Long equipmentId, LocalDate startDate) {
        StudentEntity student = studentService.getById(studentId);
        EquipmentEntity equipment = equipmentService.getById(equipmentId);

        if (!equipment.isAvailability()) {
            throw new IllegalStateException("Equipment not available.");
        }

        long activeLoans = loanRepository.countActiveLoans(student.getId());
        if (activeLoans >= 2) {
            throw new IllegalStateException("Student already has 2 active loans.");
        }

        LoanEntity loan = new LoanEntity();
        loan.setStudent(student);
        loan.setEquipment(equipment);
        loan.setStartDate(startDate);  // ✅ custom start date
        loan.setDueDate(startDate.plusDays(7)); // ✅ due date based on custom start
        loan.setStatus(LoanEntity.Status.ONGOING);

        loanRepository.save(loan);
        equipmentService.updateAvailability(equipmentId, false);

        return loan;
    }


    public double returnLoan(Long loanId, LocalDate returnDate) {
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalStateException("Loan not found"));

        // If already returned
        if (loan.getStatus() == LoanEntity.Status.RETURNED || loan.getStatus() == LoanEntity.Status.OVERDUE) {
            throw new IllegalStateException("Already returned, loan another equipment");
        }

        loan.setReturnDate(returnDate);

        if (returnDate.isAfter(loan.getDueDate())) {
            loan.setStatus(LoanEntity.Status.OVERDUE);
        } else {
            loan.setStatus(LoanEntity.Status.RETURNED);
        }

        double penalty = penaltyStrategy.calculatePenalty(loan);
        loanRepository.save(loan);

        // Mark equipment as available again
        equipmentService.updateAvailability(loan.getEquipment().getId(), true);

        return penalty;
    }


    public double calculatePenalty(Long loanId) {
        LoanEntity loan = loanRepository.findById(loanId).orElseThrow();
        return penaltyStrategy.calculatePenalty(loan);
    }

    public List<LoanEntity> getAll() {
        return loanRepository.findAll();
    }
}
