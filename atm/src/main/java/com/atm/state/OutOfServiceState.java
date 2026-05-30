package com.atm.state;

import com.atm.model.Atm;

public class OutOfServiceState extends AbstractAtmState {

    public OutOfServiceState(Atm atm) {
        super(atm);
    }

    @Override
    public void insertCard(com.atm.model.Card card) {
        throw new RuntimeException("ATM is currently out of service");
    }
}