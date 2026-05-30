package com.atm.state;

import java.math.BigDecimal;
import java.util.Map;

import com.atm.enums.Denomination;
import com.atm.model.Atm;
import com.atm.model.Card;

public abstract class AbstractAtmState implements AtmState {

    protected final Atm atm;

    protected AbstractAtmState(Atm atm) {
        this.atm = atm;
    }

    @Override
    public void insertCard(Card card) {
        throw new RuntimeException("Insert card operation not allowed in current state");
    }

    @Override
    public void authenticate(String pin) {
        throw new RuntimeException("Authentication not allowed in current state");
    }

    @Override
    public BigDecimal checkBalance() {
        throw new RuntimeException("Balance inquiry not allowed in current state");
    }

    @Override
    public void withdraw(BigDecimal amount) {
        throw new RuntimeException("Withdrawal not allowed in current state");
    }

    @Override
    public void deposit(Map<Denomination, Integer> cash) {
        throw new RuntimeException("Deposit not allowed in current state");
    }

    @Override
    public void ejectCard() {
        throw new RuntimeException("Eject card not allowed in current state");
    }
}