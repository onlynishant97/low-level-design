package com.atm.state;

import java.math.BigDecimal;
import java.util.Map;

import com.atm.enums.Denomination;
import com.atm.model.Card;

public interface AtmState {

    void insertCard(Card card);

    void authenticate(String pin);

    BigDecimal checkBalance();

    void withdraw(BigDecimal amount);

    void deposit(Map<Denomination, Integer> cash);

    void ejectCard();
}