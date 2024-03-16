package com.katanox.api.common.hotel;

import com.katanox.test.sql.tables.Hotels;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class HotelRepository {
    private final DSLContext dsl;

    public HotelRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Double getVatForHotel(long hotelId) {
        Double vat = null;
        var records = dsl.select(Hotels.HOTELS.VAT)
                .from(Hotels.HOTELS)
                .where(Hotels.HOTELS.ID.eq(hotelId))
                .fetch();

        if (!records.isEmpty()) {
            vat = records.get(0).get(Hotels.HOTELS.VAT, Double.class);
        }
        return vat;
    }
}