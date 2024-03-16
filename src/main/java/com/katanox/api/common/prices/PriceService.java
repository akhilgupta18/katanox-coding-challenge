package com.katanox.api.common.prices;

import com.katanox.api.common.hotel.HotelService;
import com.katanox.test.sql.tables.Prices;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Service
public class PriceService {
    private final HotelService hotelService;
    private final PriceRepository repository;

    public PriceService(HotelService hotelService, PriceRepository repository) {
        this.hotelService = hotelService;
        this.repository = repository;
    }

    public Map<Long, Set<Price>> findPricesByRoomForDatesInHotel(LocalDate checkin, LocalDate checkout, long hotelId) {
        Set<LocalDate> dates = intervalToDatesOfNights(checkin, checkout);
        Result<Record> records = repository.findPriceRecordsForDatesInHotel(dates, hotelId);
        double vat = hotelService.getVatForHotel(hotelId);
        Map<Long, Set<Price>> result = new HashMap<>();
        for (Record record : records) {
            Price price = recordToPrice(record, vat);
            result.computeIfAbsent(price.roomId(),
                    k -> new TreeSet<>(Comparator.comparing(Price::date)));
            result.get(price.roomId()).add(price);
        }
        return result;
    }

    public void decreaseQuantities(long roomId, LocalDate checkin, LocalDate checkout) {
        Set<LocalDate> dates = intervalToDatesOfNights(checkin, checkout);
        repository.decreaseQuantitiesForDatesForRoom(dates, roomId);
    }

    public double removeVat(double afterTax, double vat) {
        return afterTax / (1 + (vat / 100));
    }

    public Price recordToPrice(Record record, double vat) {
        double priceAfterTax = record.get(Prices.PRICES.PRICE_AFTER_TAX).doubleValue();
        double priceBeforeTax = removeVat(priceAfterTax, vat);
        return new Price(
                Currency.getInstance(record.get(Prices.PRICES.CURRENCY)),
                record.get(Prices.PRICES.DATE),
                priceAfterTax,
                priceBeforeTax,
                record.get(Prices.PRICES.QUANTITY),
                record.get(Prices.PRICES.ROOM_ID)
        );
    }

    public Set<LocalDate> intervalToDatesOfNights(LocalDate checkin, LocalDate checkout) {
        Set<LocalDate> dates = new HashSet<>();
        LocalDate dateIterator = checkin;
        while (dateIterator.isBefore(checkout)) {
            dates.add(dateIterator);
            dateIterator = dateIterator.plusDays(1);
        }
        return dates;
    }
}