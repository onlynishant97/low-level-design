package com.airline.repository;

import com.airline.model.Payment;

import java.util.Optional;

public interface PaymentRepository {

    void save(Payment payment);

    Optional<Payment> findById(String paymentId);
}