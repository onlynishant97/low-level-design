package com.carrental.strategy;

public interface PaymentStrategy {
    boolean processPayment(String paymentId, double amount);
}
