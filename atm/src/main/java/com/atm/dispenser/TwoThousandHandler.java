package com.atm.dispenser;

import java.util.Map;
import com.atm.enums.Denomination;

public class TwoThousandHandler extends CashDispenseHandler {

    @Override
    public void handle(int amount,
                       Map<Denomination, Integer> result,
                       Map<Denomination, Integer> available) {

        int availableNotes = available.getOrDefault(Denomination.TWO_THOUSAND, 0);

        int needed = amount / 2000;

        int used = Math.min(needed, availableNotes);

        if (used > 0) {
            result.put(Denomination.TWO_THOUSAND, used);
        }

        int remaining = amount - (used * 2000);

        if (next != null && remaining > 0) {
            next.handle(remaining, result, available);
        }
    }
}