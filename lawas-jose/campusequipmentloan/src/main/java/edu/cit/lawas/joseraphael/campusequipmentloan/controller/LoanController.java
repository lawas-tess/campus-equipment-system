package edu.cit.lawas.joseraphael.campusequipmentloan.controller;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.LoanEntity;
import edu.cit.lawas.joseraphael.campusequipmentloan.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // Create loan
    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody Map<String, String> payload) {
        try {
            Long studentId = Long.parseLong(payload.get("studentId"));
            Long equipmentId = Long.parseLong(payload.get("equipmentId"));

            // Use provided startDate if exists, otherwise default to today
            LocalDate startDate = payload.containsKey("startDate") && !payload.get("startDate").isBlank()
                    ? LocalDate.parse(payload.get("startDate"))
                    : LocalDate.now();

            LoanEntity loan = loanService.createLoan(studentId, equipmentId, startDate);

            return ResponseEntity.ok(Map.of(
                    "message", "Loan created successfully.",
                    "loanId", loan.getId(),
                    "studentId", studentId,
                    "equipmentId", equipmentId,
                    "startDate", loan.getStartDate().toString(),
                    "dueDate", loan.getDueDate().toString(),
                    "status", loan.getStatus().toString()
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(Map.of(
                    "error", e.getMessage(),
                    "studentId", payload.get("studentId"),
                    "equipmentId", payload.get("equipmentId"),
                    "startDate", payload.get("startDate")
            ));
        }
    }


    @PostMapping("/{loanId}/return")
    public ResponseEntity<?> returnLoan(@PathVariable Long loanId,
                                        @RequestBody Map<String, String> payload) {

        // Check if returnDate missing
        if (!payload.containsKey("returnDate") || payload.get("returnDate").isBlank()) {
            return ResponseEntity.ok(Map.of(
                    "error", "Add returnDate",
                    "loanId", loanId
            ));
        }

        LocalDate returnDate = LocalDate.parse(payload.get("returnDate"));

        try {
            double penalty = loanService.returnLoan(loanId, returnDate);

            return ResponseEntity.ok(Map.of(
                    "message", "Loan returned successfully",
                    "loanId", loanId,
                    "returnDate", returnDate.toString(),
                    "penalty", penalty
            ));
        } catch (IllegalStateException e) {
            // Always respond 200 OK
            return ResponseEntity.ok(Map.of(
                    "error", e.getMessage(),
                    "loanId", loanId,
                    "returnDate", returnDate.toString()
            ));
        }
    }
}
