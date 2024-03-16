package com.katanox.api.integrationTests;

import com.katanox.api.booking.BookingDTO;
import com.katanox.api.booking.BookingRepository;
import com.katanox.api.booking.BookingRequest;
import com.katanox.api.booking.BookingService;
import com.katanox.api.common.guest.Guest;
import com.katanox.api.common.payments.Payment;
import com.katanox.api.common.prices.PriceService;
import com.katanox.api.common.queue.RabbitMQBookingSenderService;
import com.katanox.api.common.validators.BookValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookValidator bookValidator;
    @Mock
    private PriceService priceService;
    @Mock
    private RabbitMQBookingSenderService rabbitMQBookingSenderService;
    @InjectMocks
    private BookingService bookingService;

    @Test
    public void testBooking() {
        // Mock data
        BookingRequest bookingRequest = new BookingRequest(
                LocalDate.of(2022, 3, 1),
                LocalDate.of(2022, 3, 6),
                "USD",
                new Guest(LocalDate.now(), "name", "name"),
                1L,
                new Payment("cardHolder", "cardNumber", "cvv", "01", "2022"),
                120.0,
                100.0,
                1L
                );

        BookingDTO bookingDTO = new BookingDTO(
                Currency.getInstance("USD"),
                new Guest(LocalDate.now(), "name", "name"),
                1L,
                new Payment("cardHolder", "cardNumber", "cvv", "01", "2022"),
                120.0,
                100.0,
                1L);

        // Mock behavior
        doNothing().when(priceService).decreaseQuantities(
                bookingRequest.roomId(),
                bookingRequest.checkin(),
                bookingRequest.checkout());

        when(bookingRepository.insertBooking(any(BookingDTO.class))).thenReturn(1L);

        // Call the method to test
        long bookingId = bookingService.booking(bookingRequest);

        // Verify interactions
        verify(bookValidator).validateBookRequest(bookingRequest);
        verify(priceService).decreaseQuantities(
                bookingRequest.roomId(),
                bookingRequest.checkin(),
                bookingRequest.checkout());
        verify(bookingRepository).insertBooking(bookingDTO);
        verify(rabbitMQBookingSenderService).ObjectRabbitMQSender(bookingDTO);
    }
}