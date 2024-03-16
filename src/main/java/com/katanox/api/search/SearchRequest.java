package com.katanox.api.search;

import java.time.LocalDate;

public record SearchRequest (
    LocalDate checkin,
    LocalDate checkout,
    long hotelId
) {}