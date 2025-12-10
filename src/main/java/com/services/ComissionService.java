package com.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ComissionService {

    public BigDecimal calculateCommission(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal commission = BigDecimal.ZERO;

        BigDecimal tier1Limit = BigDecimal.valueOf(1000);
        BigDecimal tier2Limit = BigDecimal.valueOf(5000);

        BigDecimal tier1Rate = BigDecimal.valueOf(0.01);   // 1%
        BigDecimal tier2Rate = BigDecimal.valueOf(0.0225); // 2.25%
        BigDecimal tier3Rate = BigDecimal.valueOf(0.05);   // 5%

        // Tier 1
        BigDecimal tier1Amount = amount.min(tier1Limit);
        commission = commission.add(tier1Amount.multiply(tier1Rate));

        // Tier 2
        if (amount.compareTo(tier1Limit) > 0) {
            BigDecimal tier2Amount = amount.min(tier2Limit).subtract(tier1Limit);
            commission = commission.add(tier2Amount.multiply(tier2Rate));
        }

        // Tier 3
        if (amount.compareTo(tier2Limit) > 0) {
            BigDecimal tier3Amount = amount.subtract(tier2Limit);
            commission = commission.add(tier3Amount.multiply(tier3Rate));
        }

        return commission.setScale(2, RoundingMode.HALF_UP);
    }

}
