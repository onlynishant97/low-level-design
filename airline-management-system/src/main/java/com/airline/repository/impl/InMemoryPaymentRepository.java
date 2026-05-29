package com.airline.repository.impl;

import com.airline.model.Payment;
import com.airline.repository.PaymentRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPaymentRepository implements PaymentRepository {

    private final ConcurrentHashMap<String, Payment> payments =
            new ConcurrentHashMap<>();

    @Override
    public void save(Payment payment) {
        payments.put(payment.getPaymentId(), payment);
    }

    @Override
    public Optional<Payment> findById(String paymentId) {
        return Optional.ofNullable(payments.get(paymentId));
    }
}