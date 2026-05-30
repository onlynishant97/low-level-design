package com.atm.dispenser;

import java.util.Map;
import com.atm.enums.Denomination;

public class OneHundredHandler extends CashDispenseHandler {

    @Override
    public void handle(int amount,
                       Map<Denomination, Integer> result,
                       Map<Denomination, Integer> available) {

        int availableNotes = available.getOrDefault(Denomination.ONE_HUNDRED, 0);

        int needed = amount / 100;

        int used = Math.min(needed, availableNotes);

        if (used > 0) {
            result.put(Denomination.ONE_HUNDRED, used);
        }

        int remaining = amount - (used * 100);

        if (next != null && remaining > 0) {
            next.handle(remaining, result, available);
        }
    }
}