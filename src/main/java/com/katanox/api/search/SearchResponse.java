package com.katanox.api.search;

import java.util.Currency;
import java.util.List;

public class SearchResponse {
    private final List<AvailableRooms> availableRooms;

    public SearchResponse(List<AvailableRooms> rooms) {
        this.availableRooms = rooms;
    }

    public List<AvailableRooms> getAvailableRooms() {
        return availableRooms;
    }
    public record AvailableRooms(
            Currency currency, long hotelId, double priceAfterTax, double priceBeforeTax, long roomId) {}
}
