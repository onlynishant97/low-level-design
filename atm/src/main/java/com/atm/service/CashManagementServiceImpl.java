package com.atm.service;

import com.atm.dispenser.*;
import com.atm.enums.Denomination;
import com.atm.hardware.CashDispenser;
import com.atm.model.CashInventory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CashManagementServiceImpl implements CashManagementService {

    private final CashInventory cashInventory;
    private final CashDispenser cashDispenser;

    public CashManagementServiceImpl(CashInventory cashInventory, CashDispenser cashDispenser) {
        this.cashInventory = cashInventory;
        this.cashDispenser = cashDispenser;
    }

    @Override
    public synchronized boolean canDispense(BigDecimal amount) {
        int requestedAmount = amount.intValue();
        Map<Denomination, Integer> available = cashInventory.getCashMap();

        // Simulate the chain without actually dispensing
        Map<Denomination, Integer> result = new HashMap<>();

        CashDispenseHandler dispenseChain = buildChain();
        dispenseChain.handle(requestedAmount, result, new HashMap<>(available));

        // Calculate total dispensed
        int totalDispensed = result.entrySet().stream()
                .mapToInt(e -> e.getKey().getValue() * e.getValue())
                .sum();

        return totalDispensed == requestedAmount;
    }

    @Override
    public synchronized Map<Denomination, Integer> dispense(BigDecimal amount) {
        int requestedAmount = amount.intValue();
        Map<Denomination, Integer> available = cashInventory.getCashMap();
        Map<Denomination, Integer> result = new HashMap<>();

        // Run through the chain
        CashDispenseHandler dispenseChain = buildChain();
        dispenseChain.handle(requestedAmount, result, new HashMap<>(available));

        // Verify full amount is covered
        int totalDispensed = result.entrySet().stream()
                .mapToInt(e -> e.getKey().getValue() * e.getValue())
                .sum();

        if (totalDispensed != requestedAmount) {
            throw new RuntimeException("ATM cannot make exact change for the requested amount.");
        }

        cashDispenser.dispense(result);
        // Only update inventory if full amount can be dispensed
        result.forEach(cashInventory::removeCash
        );

        System.out.println("Dispensing cash: " + result);
        return result;
    }

    @Override
    public synchronized void deposit(Map<Denomination, Integer> depositedCash) {
        for (Map.Entry<Denomination, Integer> entry : depositedCash.entrySet()) {
            cashInventory.addCash(entry.getKey(), entry.getValue());
        }
    }


    private CashDispenseHandler buildChain() {
        CashDispenseHandler h2000 = new TwoThousandHandler();
        CashDispenseHandler h500 = new FiveHundredHandler();
        CashDispenseHandler h200 = new TwoHundredHandler();
        CashDispenseHandler h100 = new OneHundredHandler();

        h2000.setNext(h500);
        h500.setNext(h200);
        h200.setNext(h100);

        return h2000;
    }
}