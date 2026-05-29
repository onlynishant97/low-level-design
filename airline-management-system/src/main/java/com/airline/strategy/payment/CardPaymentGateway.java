package com.airline.strategy.payment;

import java.util.Random;

public class CardPaymentGateway implements PaymentGateway {

    private final Random random = new Random();

    @Override
    public boolean processPayment(String paymentId, double amount) {
        return random.nextInt(100) >= 5;
    }
}