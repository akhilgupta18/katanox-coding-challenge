package com.katanox.api.integrationTests;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.katanox.api.common.charges.ExtraChargeService;
import com.katanox.api.common.prices.Price;
import com.katanox.api.common.prices.PriceService;
import com.katanox.api.common.validators.SearchValidator;
import com.katanox.api.search.SearchResponse;
import com.katanox.api.search.SearchRequest;
import com.katanox.api.search.SearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {
    @Mock
    private SearchValidator searchValidator;
    @Mock
    private PriceService priceService;
    @Mock
    private ExtraChargeService extraChargeService;
    @InjectMocks
    private SearchService searchService;

    @Test
    public void testSearch() {
        // Mock data
        LocalDate checkin = LocalDate.of(2022, 3, 1);
        LocalDate checkout = LocalDate.of(2022, 3, 2);
        long hotelId = 1L;

        SearchRequest request = new SearchRequest(checkin, checkout, hotelId);

        Set<Price> prices = new HashSet<>();
        prices.add(new Price(
                Currency.getInstance("USD"), checkin, 120.0, 100.0, 1, 1L));
        Map<Long, Set<Price>> pricesByRoom = Collections.singletonMap(1L, prices);

        when(priceService.findPricesByRoomForDatesInHotel(any(LocalDate.class), any(LocalDate.class), any(Long.class)))
                .thenReturn(pricesByRoom);
        when(extraChargeService.calculateCharges(any(Integer.class), any(Double.class), any(Double.class),
                any(Long.class)))
                .thenReturn(10.0);

        // Call the method to test
        SearchResponse response = searchService.search(request);

        // Verify the response
        assertEquals(1, response.getAvailableRooms().size());
        SearchResponse.AvailableRooms availableRooms = response.getAvailableRooms().get(0);
        assertEquals("USD", availableRooms.currency().getCurrencyCode());
        assertEquals(hotelId, availableRooms.hotelId());
        assertEquals(130.0, availableRooms.priceAfterTax());
        assertEquals(110.0, availableRooms.priceBeforeTax());
        assertEquals(1L, availableRooms.roomId());
    }
}