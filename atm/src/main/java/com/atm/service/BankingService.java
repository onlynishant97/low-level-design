package com.atm.service;

import java.math.BigDecimal;

import com.atm.model.Account;
import com.atm.model.Card;

public interface BankingService {

    boolean authenticate(Card card, String pin);

    Account getAccount(String accountNumber);

    void debit(String accountNumber, BigDecimal amount);

    void credit(String accountNumber, BigDecimal amount);

    BigDecimal getBalance(String accountNumber);
}