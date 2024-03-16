package com.katanox.api.booking;

import java.time.LocalDate;

import com.katanox.api.common.guest.Guest;
import com.katanox.api.common.payments.Payment;

public record BookingRequest (
    LocalDate checkin,
    LocalDate checkout,
    String currency,
    Guest guest,
    long hotelId,
    Payment payment,
    Double priceAfterTax,
    Double priceBeforeTax,
    long roomId
){}
