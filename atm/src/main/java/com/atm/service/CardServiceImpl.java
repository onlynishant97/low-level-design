package com.atm.service;

import com.atm.model.Card;
import com.atm.repository.CardRepository;

public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card findByCardNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }
}
