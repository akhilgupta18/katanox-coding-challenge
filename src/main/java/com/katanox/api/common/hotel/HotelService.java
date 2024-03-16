package com.katanox.api.common.hotel;

import org.springframework.stereotype.Service;

@Service
public class HotelService {
    HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Double getVatForHotel(long hotelId) {
        return hotelRepository.getVatForHotel(hotelId);
    }
}
