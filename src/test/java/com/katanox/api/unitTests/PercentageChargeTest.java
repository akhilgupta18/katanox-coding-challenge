package com.katanox.api.unitTests;

import com.katanox.api.common.charges.PercentageCharge;
import com.katanox.api.common.charges.PercentageChargeRepository;
import com.katanox.api.common.charges.PercentageChargeService;
import com.katanox.api.common.charges.PercentageChargeType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PercentageChargeServiceTest {
    private final PercentageChargeRepository percentageChargeRepository = Mockito.mock(PercentageChargeRepository.class);
    private final PercentageChargeService percentageChargeService = new PercentageChargeService(percentageChargeRepository);

    @Test
    void testPriceCalculationWithOutPercentageCharges() {
        long hotelId = 1L;
        double firstNightPrice = 10.0;
        double totalAmount = 30.0;
        when(percentageChargeRepository.findChargesByHotel(hotelId)).thenReturn(Collections.emptySet());
        double result = percentageChargeService.calculatePercentageCharges(hotelId, firstNightPrice, totalAmount);

        assertEquals(0.0, result);
    }

    @Test
    void testPriceCalculationWithPercentageCharges() {
        long hotelId = 1L;
        double firstNightPrice = 10.0;
        double totalAmount = 30.0;
        when(percentageChargeRepository.findChargesByHotel(hotelId)).thenReturn(getPercentageCharges());
        double result = percentageChargeService.calculatePercentageCharges(hotelId, firstNightPrice, totalAmount);

        assertEquals((0.3 * 10) + (0.2 * 30), result);
    }

    private Set<PercentageCharge> getPercentageCharges() {
        return Set.of(
                new PercentageCharge(2, "", PercentageChargeType.FIRST_NIGHT, 30.0, 1),
                new PercentageCharge(1, "", PercentageChargeType.TOTAL_AMOUNT, 20.0, 1)
        );
    }
}