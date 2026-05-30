package com.atm.state;

import com.atm.model.Atm;
import com.atm.model.Card;

public class CardInsertedState extends AbstractAtmState {

    private final Card card;

    public CardInsertedState(Atm atm, Card card) {
        super(atm);
        this.card = card;
    }

    @Override
    public void authenticate(String pin) {
        boolean authenticated =
                atm.getBankingService()
                        .authenticate(card, pin);
        if (authenticated) {
            atm.setCurrentState(new AuthenticatedState(atm, card));
        }
    }

    @Override
    public void ejectCard() {
        atm.setCurrentState(new IdleState(atm));
        System.out.println("Card ejected.");
    }
}