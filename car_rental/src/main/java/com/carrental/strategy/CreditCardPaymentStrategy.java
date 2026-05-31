package com.carrental.strategy;

public class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(String paymentId, double amount) {
        return true;
    }
}
