package edu.cit.lawas.joseraphael.campusequipmentloan.penalty;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.LoanEntity;

public class FixedPenaltyStrategy implements PenaltyStrategy {

    private static final double DAILY_PENALTY = 50.0;

    @Override
    public double calculatePenalty(LoanEntity loan) {
        if (loan.getReturnDate() == null || loan.getDueDate() == null) return 0.0;

        if (loan.getReturnDate().isAfter(loan.getDueDate())) {
            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(
                    loan.getDueDate(), loan.getReturnDate()
            );
            return daysLate * DAILY_PENALTY;
        }
        return 0.0;
    }
}
