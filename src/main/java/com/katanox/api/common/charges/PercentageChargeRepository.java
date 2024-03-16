package com.katanox.api.common.charges;

import com.katanox.test.sql.tables.ExtraChargesPercentage;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class PercentageChargeRepository {
    private final DSLContext dsl;

    public PercentageChargeRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Set<PercentageCharge> findChargesByHotel(long hotelId) {
        Set<PercentageCharge> charges = new HashSet<>();
        dsl.select()
                .from(ExtraChargesPercentage.EXTRA_CHARGES_PERCENTAGE)
                .where(ExtraChargesPercentage.EXTRA_CHARGES_PERCENTAGE.HOTEL_ID.eq(hotelId))
                .fetch()
                .map(record -> charges.add(PercentageCharge.ofRecord(record)));
        return charges;
    }
}