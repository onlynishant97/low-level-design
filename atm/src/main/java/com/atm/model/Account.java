package com.atm.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class Account {
    private final String accountNumber;
    BigDecimal balance;
    private final ReentrantLock lock;

    public Account(String accountNumber, BigDecimal initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.lock = new ReentrantLock();
    }

    public void debit(BigDecimal amount) {
        lock.lock();
        try {
            if (balance.compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            balance = balance.subtract(amount);
        } finally {
            lock.unlock();
        }
    }

    public void credit(BigDecimal amount) {
        lock.lock();
        try {
            balance = balance.add(amount);
        } finally {
            lock.unlock();
        }
    }
}
