package com.katanox.api.booking;

import java.util.Currency;

import com.katanox.api.common.guest.Guest;
import com.katanox.api.common.payments.Payment;

public record BookingDTO (
        Currency currency,
        Guest guest,
        Long hotelId,
        Payment payment,
        Double priceAfterTax,
        Double priceBeforeTax,
        Long roomId
) {}
