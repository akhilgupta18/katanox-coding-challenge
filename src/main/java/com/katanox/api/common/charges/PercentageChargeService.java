package com.katanox.api.common.charges;

import java.util.Set;

import org.springframework.stereotype.Service;

import static com.katanox.api.common.charges.PercentageChargeType.FIRST_NIGHT;
import static com.katanox.api.common.charges.PercentageChargeType.TOTAL_AMOUNT;

@Service
public class PercentageChargeService {
    private final PercentageChargeRepository repository;

    public PercentageChargeService(PercentageChargeRepository repository) {
        this.repository = repository;
    }

    public double calculatePercentageCharges(long hotelId, double firstNightPrice, double totalAmount) {
        double result = 0.0;
        Set<PercentageCharge> percentageCharges = repository.findChargesByHotel(hotelId);
        for (PercentageCharge percentageCharge : percentageCharges) {
            result += percentageCharge.chargeType() == FIRST_NIGHT
                    ? (percentageCharge.percentage() / 100.0) * firstNightPrice : 0.0;
            result += percentageCharge.chargeType() == TOTAL_AMOUNT
                    ? (percentageCharge.percentage() / 100.0) * totalAmount : 0.0;
        }
        return result;
    }
}
