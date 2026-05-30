package com.atm.repository;

import com.atm.model.Transaction;
import java.util.List;

public interface TransactionRepository {
    void save(Transaction transaction);
    List<Transaction> findByAccountNumber(String accountNumber);
}