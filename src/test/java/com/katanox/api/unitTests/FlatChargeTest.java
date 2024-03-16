package com.katanox.api.unitTests;

import com.katanox.api.common.charges.FlatCharge;
import com.katanox.api.common.charges.FlatChargeRepository;
import com.katanox.api.common.charges.FlatChargeService;
import com.katanox.api.common.charges.FlatChargeType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Currency;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class FlatChargeServiceTest {
    private final FlatChargeRepository flatChargeRepository = Mockito.mock(FlatChargeRepository.class);
    private final FlatChargeService flatChargeService = new FlatChargeService(flatChargeRepository);

    @Test
    void testPriceCalculationWithOutFlatCharges() {
        long hotelId = 1L;
        int numberOfNights = 3;
        when(flatChargeRepository.findChargesByHotel(hotelId)).thenReturn(Collections.emptySet());
        double result = flatChargeService.calculateFlatCharges(hotelId, numberOfNights);

        assertEquals(0.0, result);
    }

    @Test
    void testPriceCalculationWithFlatCharges() {
        long hotelId = 1L;
        int numberOfNights = 3;
        when(flatChargeRepository.findChargesByHotel(hotelId)).thenReturn(getFlatCharges());
        double result = flatChargeService.calculateFlatCharges(hotelId, numberOfNights);

        assertEquals(20.0 + (3 * 10), result);
    }

    private Set<FlatCharge> getFlatCharges() {
        return Set.of(
                new FlatCharge(1, "", FlatChargeType.ONCE, 20.0, Currency.getInstance("EUR"), 1),
                new FlatCharge(2, "", FlatChargeType.PER_NIGHT, 10.0, Currency.getInstance("EUR"), 1)
        );
    }
}