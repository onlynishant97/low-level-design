package com.atm.service;

import com.atm.enums.TransactionType;
import com.atm.model.Account;
import com.atm.model.Card;
import com.atm.model.Transaction;
import com.atm.repository.AccountRepository;
import com.atm.repository.TransactionRepository;

import java.math.BigDecimal;

public class BankingServiceImpl implements BankingService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public BankingServiceImpl(AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public boolean authenticate(Card card, String pin) {
        if (!"1234".equals(pin)) {
            throw new RuntimeException("Invalid PIN");
        }
        return true;
    }

    @Override
    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public void debit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository
                .findByAccountNumber(accountNumber);
        try {
            account.debit(amount);
            transactionRepository.save(
                    new Transaction(accountNumber, TransactionType.WITHDRAWAL, amount, true)
            );
        } catch (RuntimeException e) {
            transactionRepository.save(
                    new Transaction(accountNumber, TransactionType.WITHDRAWAL, amount, false)
            );
            throw e;
        }
    }

    @Override
    public void credit(String accountNumber, BigDecimal amount) {
        Account account = getAccount(accountNumber);

        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        try {
            account.credit(amount);
            transactionRepository.save(
                    new Transaction(accountNumber, TransactionType.DEPOSIT, amount, true)
            );
        } catch (RuntimeException e) {
            transactionRepository.save(
                    new Transaction(accountNumber, TransactionType.DEPOSIT, amount, false)
            );
            throw e;
        }
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        BigDecimal balance = getAccount(accountNumber).getBalance();
        // Log balance inquiry
        transactionRepository.save(
                new Transaction(accountNumber, TransactionType.BALANCE_INQUIRY, balance, true)
        );
        return balance;
    }
}