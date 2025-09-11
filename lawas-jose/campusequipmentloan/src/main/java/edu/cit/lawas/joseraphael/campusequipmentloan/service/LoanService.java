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
        this.penaltyStrategy = new FixedPenaltyStrategy(); // Strategy in use
    }

    // Create a new loan
    public void createLoan(Long studentId, Long equipmentId) {
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
        loan.setStartDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(7));
        loan.setStatus(LoanEntity.Status.ONGOING);

        loanRepository.save(loan);
        equipmentService.updateAvailability(equipmentId, false);
    }

    // Return loan
    public void returnLoan(Long loanId) {
        LoanEntity loan = loanRepository.findById(loanId);
        loan.setReturnDate(LocalDate.now());

        if (loan.getReturnDate().isAfter(loan.getDueDate())) {
            loan.setStatus(LoanEntity.Status.OVERDUE);
        } else {
            loan.setStatus(LoanEntity.Status.RETURNED);
        }

        loanRepository.updateStatus(loan.getId(), loan.getStatus(), loan.getReturnDate());
        equipmentService.updateAvailability(loan.getEquipment().getId(), true);
    }

    // Calculate penalty
    public double calculatePenalty(Long loanId) {
        LoanEntity loan = loanRepository.findById(loanId);
        return penaltyStrategy.calculatePenalty(loan);
    }

    // Get all loans
    public List<LoanEntity> getAll() {
        return loanRepository.findAll();
    }
}
