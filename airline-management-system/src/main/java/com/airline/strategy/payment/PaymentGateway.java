package com.airline.strategy.payment;

public interface PaymentGateway {

    boolean processPayment(String paymentId, double amount);
}