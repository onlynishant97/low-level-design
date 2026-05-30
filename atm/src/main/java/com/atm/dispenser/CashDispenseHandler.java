package com.atm.dispenser;

import java.util.Map;
import com.atm.enums.Denomination;

public abstract class CashDispenseHandler {

    protected CashDispenseHandler next;

    public void setNext(CashDispenseHandler next) {
        this.next = next;
    }

    public abstract void handle(
            int amount,
            Map<Denomination, Integer> result,
            Map<Denomination, Integer> available
    );
}