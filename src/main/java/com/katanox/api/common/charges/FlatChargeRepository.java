package com.katanox.api.common.charges;

import com.katanox.test.sql.tables.ExtraChargesFlat;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class FlatChargeRepository {
    private final DSLContext dsl;

    public FlatChargeRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Set<FlatCharge> findChargesByHotel(long hotelId) {
        Set<FlatCharge> charges = new HashSet<>();
        dsl.select()
                .from(ExtraChargesFlat.EXTRA_CHARGES_FLAT)
                .where(ExtraChargesFlat.EXTRA_CHARGES_FLAT.HOTEL_ID.eq(hotelId))
                .fetch()
                .map(record -> charges.add(FlatCharge.ofRecord(record)));
        return charges;
    }
}