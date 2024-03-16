package com.katanox.api.unitTests;

import com.katanox.api.common.charges.ExtraChargeService;
import com.katanox.api.common.prices.Price;
import com.katanox.api.common.prices.PriceService;
import com.katanox.api.common.validators.SearchValidator;
import com.katanox.api.search.*;
import com.katanox.api.search.SearchResponse.AvailableRooms;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

class SearchServiceTest {

    private final SearchValidator searchValidator = Mockito.mock(SearchValidator.class);
    private final PriceService priceService = Mockito.mock(PriceService.class);
    private final ExtraChargeService extraChargeService = Mockito.mock(ExtraChargeService.class);
    private final SearchService searchService = new SearchService(searchValidator, priceService, extraChargeService);

    @Test
    void testPricesWithNoExtraCharges() {
        SearchRequest request = initMocksAndSearchRequest();
        when(extraChargeService.calculateCharges(1, 100.0, 40.0, 1)).thenReturn(0.0);
        SearchResponse result = searchService.search(request);

        assertEquals(2, result.getAvailableRooms().size());
        Double[] resultPrices = result.getAvailableRooms()
                .stream().map(AvailableRooms::priceBeforeTax).sorted().toArray(Double[]::new);
        Double[] expectedPrices = {3.0, 7.0};
        assertArrayEquals(expectedPrices, resultPrices);
    }

    @Test
    void testPricesWithExtraCharge() {
        SearchRequest request = initMocksAndSearchRequest();
        when(extraChargeService.calculateCharges(1, 100.0, 40.0, 1)).thenReturn(3.0, 4.0);
        SearchResponse result = searchService.search(request);

        assertEquals(2, result.getAvailableRooms().size());
        Double[] resultPrices = result.getAvailableRooms()
                .stream().map(AvailableRooms::priceBeforeTax).sorted().toArray(Double[]::new);
        Double[] expectedPrices = {3.0, 7.0};
        assertArrayEquals(expectedPrices, resultPrices);
    }

    private SearchRequest initMocksAndSearchRequest() {
        LocalDate checkin = LocalDate.of(2022, 7, 11);
        LocalDate checkout = LocalDate.of(2022, 7, 13);
        long hotelId = 1;
        SearchRequest request = new SearchRequest(checkin, checkout, hotelId);
        doAnswer(invocation -> null).when(searchValidator).validateSearchRequest(request);
        when(priceService.findPricesByRoomForDatesInHotel(checkin, checkout, hotelId)).thenReturn(getRoomsToPricesMap());
        return request;
    }

    private Map<Long, Set<Price>> getRoomsToPricesMap() {
        return Map.of(
                1L, Set.of(
                        getPrice(1L, 1L, LocalDate.of(2022, 7, 11)),
                        getPrice(1L, 2L, LocalDate.of(2022, 7, 12))
                ),
                2L, Set.of(
                        getPrice(2L, 3L, LocalDate.of(2022, 7, 11)),
                        getPrice(2L, 4L, LocalDate.of(2022, 7, 12))
                )
        );
    }

    private Price getPrice(long id, long price, LocalDate date) {
        return new Price(
                Currency.getInstance("EUR"), date, 1.2 * price, price, 1, id);
    }
}