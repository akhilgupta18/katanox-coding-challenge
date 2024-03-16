package com.katanox.api.common.charges;

import com.katanox.test.sql.enums.AppliedOn;
import org.jooq.Record;
import com.katanox.test.sql.tables.ExtraChargesPercentage;

import static com.katanox.api.common.charges.PercentageChargeType.*;

public record PercentageCharge(
        long id, String description, PercentageChargeType chargeType, double percentage, long hotelId) {

    public static PercentageCharge ofRecord(Record record) {
        return new PercentageCharge(
                record.get(ExtraChargesPercentage.EXTRA_CHARGES_PERCENTAGE.ID),
                record.get(ExtraChargesPercentage.EXTRA_CHARGES_PERCENTAGE.DESCRIPTION),
                ofAppliedOn(record.get(ExtraChargesPercentage.EXTRA_CHARGES_PERCENTAGE.APPLIED_ON)),
                record.get(ExtraChargesPercentage.EXTRA_CHARGES_PERCENTAGE.PERCENTAGE),
                record.get(ExtraChargesPercentage.EXTRA_CHARGES_PERCENTAGE.HOTEL_ID));
    }

    private static PercentageChargeType ofAppliedOn(AppliedOn appliedOn) {
        return switch (appliedOn) {
            case first_night -> FIRST_NIGHT;
            case total_amount -> TOTAL_AMOUNT;
        };
    }
}
