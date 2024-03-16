package com.katanox.api.common.validators;

import java.time.LocalDate;
import java.time.Year;
import java.util.Currency;

import com.katanox.api.booking.BookingRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class BookValidator {
    public void validateBookRequest(BookingRequest request) {
        if (request.checkout().isBefore(request.checkin().plusDays(1))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Checkin date must be at least 1 day before checkout date.");
        }

        if (request.currency().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please fill in a valid Currency.");
        } else {
            try {
                Currency.getInstance(request.currency());
            } catch (Exception ex) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Please fill in a valid Currency like EUR, USD etc.");
            }
        }

        if (request.guest().birthdate() == null
                || request.guest().birthdate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please fill in a valid Guest - Birthdate.");
        }

        if (request.guest().name().isEmpty() || request.guest().name().length() > 255) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please fill in a valid Guest - Name.");
        }

        if (request.guest().surname().isEmpty() || request.guest().surname().length() > 255) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please fill in a valid Guest - Surname.");
        }

        if (request.payment().cardHolder().isEmpty() || request.payment().cardHolder().length() > 255) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please fill in a valid Payment - Card Holder.");
        }

        if (request.payment().cardNumber().length() != 12
                || !request.payment().cardNumber().chars().allMatch(Character::isDigit)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please fill in a valid Payment - Card Number.");
        }

        if (request.payment().cvv().isEmpty()
                || !request.payment().cvv().chars().allMatch(Character::isDigit)
                || Integer.parseInt(request.payment().cvv()) < 100
                || Integer.parseInt(request.payment().cvv()) > 999) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please fill in a valid Payment - cvv.");
        }

        if (request.payment().expiryMonth().isEmpty()
                || !request.payment().expiryMonth().chars().allMatch(Character::isDigit)
                || Integer.parseInt(request.payment().expiryMonth()) < 0
                || Integer.parseInt(request.payment().expiryMonth()) > 12) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please fill in a valid Payment - Expiry Month.");
        }

        if (request.payment().expiryYear().isEmpty()
                || !request.payment().expiryYear().chars().allMatch(Character::isDigit)
                || Integer.parseInt(request.payment().expiryYear()) < Year.now().getValue()
                || Integer.parseInt(request.payment().expiryYear()) > Year.now().getValue() + 50) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please fill in a valid Payment - Expiry Year.");
        }
    }
}
