package com.carrental.service;

import com.carrental.enums.PaymentStatus;
import com.carrental.factory.PaymentStrategyFactory;
import com.carrental.model.Payment;
import com.carrental.repository.PaymentRepository;

import java.util.UUID;

public class PaymentService {
    private final PaymentRepository paymentRepository;


    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment processPayment(double amount, String provider) {
        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                amount
        );
        boolean paymentStatus = PaymentStrategyFactory.getPaymentStrategy(provider).processPayment(payment.getPaymentId(), amount);
        if (paymentStatus) {
            payment.markSuccess();
        } else {
            payment.markFailed();
        }
        paymentRepository.save(payment);
        return payment;
    }

    public void refund(Payment payment) {
        payment.markRefunded();
        paymentRepository.save(payment);
    }
}
