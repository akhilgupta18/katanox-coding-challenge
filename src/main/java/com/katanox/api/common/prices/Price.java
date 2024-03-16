package com.katanox.api.common.prices;

import java.time.LocalDate;
import java.util.Currency;

public record Price (
    Currency currency,
    LocalDate date,
    double priceAfterTax,
    double priceBeforeTax,
    int quantity,
    long roomId) {}
