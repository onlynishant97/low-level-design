package com.atm.repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.atm.model.Card;

public class InMemoryCardRepository implements CardRepository {

    private final ConcurrentMap<String, Card> cards = new ConcurrentHashMap<>();

    @Override
    public Card findByCardNumber(String cardNumber) {
        return cards.get(cardNumber);
    }

    @Override
    public void save(Card card) {
        cards.put(card.getCardNumber(), card);
    }
}