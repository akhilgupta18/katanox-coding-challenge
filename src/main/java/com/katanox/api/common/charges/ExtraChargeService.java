package com.katanox.api.common.charges;

import org.springframework.stereotype.Service;

@Service
public class ExtraChargeService {
    private final FlatChargeService flatChargeService;
    private final PercentageChargeService percentageChargeService;

    public ExtraChargeService(FlatChargeService flatChargeService, PercentageChargeService percentageChargeService) {
        this.flatChargeService = flatChargeService;
        this.percentageChargeService = percentageChargeService;
    }

    public double calculateCharges(
            int numberOfNights,
            double firstNightPrice,
            double roomPrice,
            long hotelId
    ) {
        double flatCharges = flatChargeService.calculateFlatCharges(hotelId, numberOfNights);
        double percentageCharges = percentageChargeService.calculatePercentageCharges(
                hotelId, firstNightPrice, roomPrice);

        return flatCharges + percentageCharges;
    }
}
