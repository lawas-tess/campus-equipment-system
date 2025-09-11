package edu.cit.lawas.joseraphael.campusequipmentloan.penalty;

import edu.cit.lawas.joseraphael.campusequipmentloan.entity.LoanEntity;

public interface PenaltyStrategy {
    double calculatePenalty(LoanEntity loan);
}

