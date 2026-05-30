package com.atm.repository;

import com.atm.model.Card;

public interface CardRepository {

    Card findByCardNumber(String cardNumber);

    void save(Card card);
}