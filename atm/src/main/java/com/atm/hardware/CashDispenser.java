package com.atm.hardware;

import java.util.Map;

import com.atm.enums.Denomination;

public class CashDispenser {

    public void dispense(Map<Denomination, Integer> notes) {
        System.out.println("Dispensing cash:");

        for (Map.Entry<Denomination, Integer> entry : notes.entrySet()) {
            System.out.println(
                    entry.getKey().getValue() + " x " + entry.getValue()
            );
        }
    }
}