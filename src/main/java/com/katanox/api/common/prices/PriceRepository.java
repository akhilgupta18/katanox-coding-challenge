package com.katanox.api.common.prices;

import com.katanox.test.sql.tables.Prices;
import com.katanox.test.sql.tables.Rooms;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.UpdateConditionStep;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class PriceRepository {
    private final DSLContext dsl;

    public PriceRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Result<Record> findPriceRecordsForDatesInHotel(Set<LocalDate> dates, long hotelId) {
        return dsl.select()
                .from(Prices.PRICES)
                .join(Rooms.ROOMS)
                .on(Prices.PRICES.ROOM_ID.eq(Rooms.ROOMS.ID))
                .where(Rooms.ROOMS.HOTEL_ID.eq(hotelId))
                .and(Prices.PRICES.DATE.in(dates))
                .and(Prices.PRICES.QUANTITY.greaterThan(0))
                .fetch();
    }

    public void decreaseQuantitiesForDatesForRoom(Set<LocalDate> dates, long roomId) {
        Result<Record> records = findPriceRecordsForDatesForRoom(dates, roomId);
        if (records.isEmpty() || records.size() < dates.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Room is not available anymore for the given dates.");
        }
        updateRoomQuantities(records);
    }

    private Result<Record> findPriceRecordsForDatesForRoom(Set<LocalDate> dates, long roomId) {
        return dsl.select()
                .from(Prices.PRICES)
                .where(Prices.PRICES.ROOM_ID.eq(roomId))
                .and(Prices.PRICES.DATE.in(dates))
                .and(Prices.PRICES.QUANTITY.greaterThan(0))
                .fetch();
    }

    private void updateRoomQuantities(Result<Record> records) {
        List<UpdateConditionStep<?>> updates = new ArrayList<>();
        records.forEach(record -> {
            updates.add(dsl.update(Prices.PRICES)
                    .set(Prices.PRICES.QUANTITY, record.get(Prices.PRICES.QUANTITY) - 1)
                    .where(Prices.PRICES.ROOM_ID.eq(record.get(Prices.PRICES.ROOM_ID)))
                    .and(Prices.PRICES.DATE.eq(record.get(Prices.PRICES.DATE))));
        });
        dsl.batch(updates).execute();
    }
}