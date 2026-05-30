package com.atm.repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.atm.model.Account;

public class InMemoryAccountRepository implements AccountRepository {

    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Account findByAccountNumber(String accountNumber) {
        return accounts.get(accountNumber);
    }

    @Override
    public void save(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }
}