package com.atm.hardware;

import java.util.Map;

import com.atm.enums.Denomination;

public class CashDeposit {

    public void acceptDeposit(Map<Denomination, Integer> depositedCash) {
        System.out.println("Cash deposited:");

        for (Map.Entry<Denomination, Integer> entry : depositedCash.entrySet()) {
            System.out.println(
                    entry.getKey().getValue() + " x " + entry.getValue()
            );
        }
    }
}