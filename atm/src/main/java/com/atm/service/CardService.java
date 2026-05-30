package com.atm.service;

import com.atm.model.Card;

public interface CardService {
    Card findByCardNumber(String cardNumber);

    void save(Card card);
}
