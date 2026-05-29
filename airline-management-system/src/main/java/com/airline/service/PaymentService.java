package com.airline.service;

import com.airline.model.Payment;

public interface PaymentService {

    Payment processPayment(double amount, String provider);

    void refund(Payment payment);
}