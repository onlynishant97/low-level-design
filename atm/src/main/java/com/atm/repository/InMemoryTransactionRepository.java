package com.atm.repository;

import com.atm.model.Transaction;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryTransactionRepository implements TransactionRepository {

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<Transaction>> store
            = new ConcurrentHashMap<>();

    @Override
    public void save(Transaction transaction) {
        store.computeIfAbsent(
                transaction.getAccountNumber(),
                k -> new CopyOnWriteArrayList<>()
        ).add(transaction);
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        return store.getOrDefault(accountNumber, new CopyOnWriteArrayList<>());
    }
}