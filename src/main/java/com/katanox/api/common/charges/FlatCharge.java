package com.katanox.api.common.charges;

import java.util.Currency;

import com.katanox.test.sql.enums.ChargeType;
import com.katanox.test.sql.tables.ExtraChargesFlat;
import org.jooq.Record;

import static com.katanox.api.common.charges.FlatChargeType.*;

public record FlatCharge(
        long id, String description, FlatChargeType chargeType, double price, Currency currency, long hotelId) {

    public static FlatCharge ofRecord(Record record) {
        return new FlatCharge(
                record.get(ExtraChargesFlat.EXTRA_CHARGES_FLAT.ID),
                record.get(ExtraChargesFlat.EXTRA_CHARGES_FLAT.DESCRIPTION),
                ofChargeType(record.get(ExtraChargesFlat.EXTRA_CHARGES_FLAT.CHARGE_TYPE)),
                record.get(ExtraChargesFlat.EXTRA_CHARGES_FLAT.PRICE),
                Currency.getInstance(record.get(ExtraChargesFlat.EXTRA_CHARGES_FLAT.CURRENCY)),
                record.get(ExtraChargesFlat.EXTRA_CHARGES_FLAT.HOTEL_ID));
    }

    private static FlatChargeType ofChargeType(ChargeType chargeType) {
        return switch (chargeType) {
            case per_night -> PER_NIGHT;
            case once -> ONCE;
        };
    }
}
