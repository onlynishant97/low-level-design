package com.atm.dispenser;

import java.util.Map;
import com.atm.enums.Denomination;

public class FiveHundredHandler extends CashDispenseHandler {

    @Override
    public void handle(int amount,
                       Map<Denomination, Integer> result,
                       Map<Denomination, Integer> available) {

        int availableNotes = available.getOrDefault(Denomination.FIVE_HUNDRED, 0);

        int needed = amount / 500;

        int used = Math.min(needed, availableNotes);

        if (used > 0) {
            result.put(Denomination.FIVE_HUNDRED, used);
        }

        int remaining = amount - (used * 500);

        if (next != null && remaining > 0) {
            next.handle(remaining, result, available);
        }
    }
}