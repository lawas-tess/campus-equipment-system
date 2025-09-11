package edu.cit.lawas.joseraphael.campusequipmentloan.controller;
import edu.cit.lawas.joseraphael.campusequipmentloan.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<String> createLoan(@RequestBody Map<String, Long> payload) {
        Long studentId = payload.get("studentId");
        Long equipmentId = payload.get("equipmentId");

        loanService.createLoan(studentId, equipmentId);
        return ResponseEntity.ok("Loan created successfully!");
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<String> returnLoan(@PathVariable Long id) {
        loanService.returnLoan(id);
        double penalty = loanService.calculatePenalty(id);

        if (penalty > 0) {
            return ResponseEntity.ok("Loan returned. Penalty = ₱" + penalty);
        }
        return ResponseEntity.ok("Loan returned successfully!");
    }
}
