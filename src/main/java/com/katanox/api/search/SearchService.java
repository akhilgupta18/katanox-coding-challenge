package com.katanox.api.search;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.katanox.api.common.validators.SearchValidator;
import com.katanox.api.search.SearchResponse.AvailableRooms;
import com.katanox.api.common.charges.ExtraChargeService;
import com.katanox.api.common.prices.Price;
import com.katanox.api.common.prices.PriceService;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    SearchValidator searchValidator;
    PriceService priceService;
    ExtraChargeService extraChargeService;

    public SearchService(
            SearchValidator searchValidator,
            PriceService priceService,
            ExtraChargeService extraChargeService) {
        this.searchValidator = searchValidator;
        this.priceService = priceService;
        this.extraChargeService = extraChargeService;
    }

    public SearchResponse search(SearchRequest request) {
        searchValidator.validateSearchRequest(request);
        long hotelId = request.hotelId();
        Map<Long, Set<Price>> pricesByRoom = priceService.findPricesByRoomForDatesInHotel(
                request.checkin(), request.checkout(), hotelId);

        int nights = (int) ChronoUnit.DAYS.between(request.checkin(), request.checkout());

        Map<Long, Set<Price>> availableRooms = pricesByRoom.entrySet().stream()
                .filter(entry -> entry.getValue().size() == nights)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<AvailableRooms> response = availableRooms.entrySet().stream()
                .map(entry -> calculatePricesAndCreateRoomResponse(entry, hotelId, nights))
                .toList();

        return new SearchResponse(response);
    }

    private AvailableRooms calculatePricesAndCreateRoomResponse(
            Entry<Long, Set<Price>> availableRooms,
            long hotelId,
            int nights
    ) {
        double roomPriceBeforeTax = availableRooms.getValue().stream().mapToDouble(Price::priceBeforeTax).sum();
        double roomPriceAfterTax = availableRooms.getValue().stream().mapToDouble(Price::priceAfterTax).sum();
        double extraCharges = extraChargeService.calculateCharges(
                nights, availableRooms.getValue().iterator().next().priceBeforeTax(), roomPriceBeforeTax, hotelId);
        return new AvailableRooms(
                availableRooms.getValue().iterator().next().currency(),
                hotelId,
                Math.round((roomPriceAfterTax + extraCharges) * 100.0) / 100.0,
                Math.round((roomPriceBeforeTax + extraCharges) * 100.0) / 100.0,
                availableRooms.getKey()
                );
    }
}
