package com.katanox.api.common.charges;

import java.util.Set;

import org.springframework.stereotype.Service;

import static com.katanox.api.common.charges.FlatChargeType.ONCE;
import static com.katanox.api.common.charges.FlatChargeType.PER_NIGHT;

@Service
public class FlatChargeService {
    private final FlatChargeRepository repository;

    public FlatChargeService(FlatChargeRepository repository) {
        this.repository = repository;
    }

    public double calculateFlatCharges(long hotelId, int numberOfNights) {
        double result = 0.0;
        Set<FlatCharge> flatCharges = repository.findChargesByHotel(hotelId);
        for (FlatCharge flatCharge : flatCharges) {
            result += flatCharge.chargeType() == ONCE
                    ? flatCharge.price() : 0.0;
            result += flatCharge.chargeType() == PER_NIGHT
                    ? flatCharge.price() * numberOfNights : 0.0;
        }

        return result;
    }
}
