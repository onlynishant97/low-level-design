package com.atm.model;

import com.atm.enums.Denomination;
import lombok.Data;

import java.util.EnumMap;
import java.util.Map;

@Data
public class CashInventory {

    private final Map<Denomination, Integer> cashMap;

    public CashInventory() {
        this.cashMap = new EnumMap<>(Denomination.class);
        for (Denomination denomination : Denomination.values()) {
            cashMap.put(denomination, 0);
        }
    }

    public void addCash(Denomination denomination, int count) {
        cashMap.put(denomination, cashMap.get(denomination) + count);
    }

    public void removeCash(Denomination denomination, int count) {
        int current = cashMap.get(denomination);
        if (current < count) {
            throw new IllegalArgumentException("Insufficient denomination count");
        }
        cashMap.put(denomination, current - count);
    }

}