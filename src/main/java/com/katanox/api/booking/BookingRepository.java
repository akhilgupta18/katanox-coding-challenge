package com.katanox.api.booking;

import java.math.BigDecimal;

import com.katanox.test.sql.tables.Bookings;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class BookingRepository {
    private final DSLContext dsl;

    public BookingRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public long insertBooking(BookingDTO bookingDto) {
        return dsl.insertInto(
                        Bookings.BOOKINGS,
                        Bookings.BOOKINGS.HOTEL_ID,
                        Bookings.BOOKINGS.ROOM_ID,
                        Bookings.BOOKINGS.GUEST_NAME,
                        Bookings.BOOKINGS.GUEST_SURNAME,
                        Bookings.BOOKINGS.PAYMENT_CARDHOLDER,
                        Bookings.BOOKINGS.PAYMENT_CARD_NUMBER,
                        Bookings.BOOKINGS.PAYMENT_CVV,
                        Bookings.BOOKINGS.PAYMENT_EXPIRY_MONTH,
                        Bookings.BOOKINGS.PAYMENT_EXPIRY_YEAR,
                        Bookings.BOOKINGS.PRICE_BEFORE_TAX,
                        Bookings.BOOKINGS.PRICE_AFTER_TAX,
                        Bookings.BOOKINGS.CURRENCY
                ).values(
                        bookingDto.hotelId(),
                        bookingDto.roomId(),
                        bookingDto.guest().name(),
                        bookingDto.guest().surname(),
                        bookingDto.payment().cardHolder(),
                        bookingDto.payment().cardNumber(),
                        bookingDto.payment().cvv(),
                        bookingDto.payment().expiryMonth(),
                        bookingDto.payment().expiryYear(),
                        BigDecimal.valueOf(bookingDto.priceBeforeTax()),
                        BigDecimal.valueOf(bookingDto.priceAfterTax()),
                        bookingDto.currency().getCurrencyCode()
                ).returningResult(Bookings.BOOKINGS.ID)
                .fetchOne()
                .get(Bookings.BOOKINGS.ID);
    }
}