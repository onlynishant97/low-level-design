package com.atm.state;

import com.atm.model.Atm;
import com.atm.model.Card;

public class IdleState extends AbstractAtmState {

    public IdleState(Atm atm) {
        super(atm);
    }

    @Override
    public void insertCard(Card card) {
        Card foundCard = atm.getCardService().findByCardNumber(card.getCardNumber());
        System.out.println("Card Found:: " + foundCard);
        atm.setCurrentState(new CardInsertedState(atm, card));
        System.out.println("Card inserted successfully.");
    }
}