package com.katanox.api.booking;

import java.util.Currency;

import com.katanox.api.common.prices.PriceService;
import com.katanox.api.common.queue.RabbitMQBookingSenderService;
import com.katanox.api.common.validators.BookValidator;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookValidator bookValidator;
    private final PriceService priceService;
    private final RabbitMQBookingSenderService rabbitMQBookingSenderService;

    public BookingService(
            BookingRepository bookingRepository,
            BookValidator bookValidator,
            PriceService priceService,
            RabbitMQBookingSenderService rabbitMQBookingSenderService) {
        this.bookingRepository = bookingRepository;
        this.bookValidator = bookValidator;
        this.rabbitMQBookingSenderService = rabbitMQBookingSenderService;
        this.priceService = priceService;
    }

    public long booking(BookingRequest bookingRequest) {
        bookValidator.validateBookRequest(bookingRequest);
        priceService.decreaseQuantities(bookingRequest.roomId(), bookingRequest.checkin(), bookingRequest.checkout());
        //Map Request to BookingDTO
        var bookingDTO = convertRequestToDTO(bookingRequest);
        long bookingId = bookingRepository.insertBooking(bookingDTO);
        rabbitMQBookingSenderService.ObjectRabbitMQSender(bookingDTO);
        return bookingId;
    }

    BookingDTO convertRequestToDTO(BookingRequest request) {
        return new BookingDTO(
                Currency.getInstance(request.currency()),
                request.guest(),
                request.hotelId(),
                request.payment(),
                request.priceAfterTax(),
                request.priceBeforeTax(),
                request.roomId());
    }
}
