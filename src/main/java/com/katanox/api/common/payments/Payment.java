package com.katanox.api.common.payments;

public record Payment (
    String cardHolder,
    String cardNumber,
    String cvv,
    String expiryMonth,
    String expiryYear) {}
