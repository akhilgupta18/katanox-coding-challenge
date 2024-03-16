package com.katanox.api.unitTests;

import com.katanox.api.common.charges.ExtraChargeService;
import com.katanox.api.common.charges.FlatChargeService;
import com.katanox.api.common.charges.PercentageChargeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ExtraChargeServiceTest {
    private final FlatChargeService flatChargeService = Mockito.mock(FlatChargeService.class);
    private final PercentageChargeService percentageChargeService = Mockito.mock(PercentageChargeService.class);
    private final ExtraChargeService extraChargeService = new ExtraChargeService(flatChargeService, percentageChargeService);

    @Test
    void calculateChargesSumsFlatAndPercentageCharges() {
        int numberOfNights = 3;
        double firstNightPrice = 10.0;
        double roomPrice = 40.0;
        long hotelId = 1L;

        when(flatChargeService.calculateFlatCharges(hotelId, numberOfNights)).thenReturn(20.0);
        when(percentageChargeService.calculatePercentageCharges(hotelId, firstNightPrice, roomPrice)).thenReturn(30.0);

        double result = extraChargeService.calculateCharges(numberOfNights, firstNightPrice, roomPrice, hotelId);

        assertEquals(BigDecimal.valueOf(50.0).doubleValue(), result);
    }
}