package com.carrental.repository;

import com.carrental.model.Payment;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentRepository {

    private final ConcurrentHashMap<String, Payment> payments =
            new ConcurrentHashMap<>();

    public void save(Payment payment) {
        payments.put(payment.getPaymentId(), payment);
    }

    public Optional<Payment> findById(String paymentId) {
        return Optional.ofNullable(payments.get(paymentId));
    }
}