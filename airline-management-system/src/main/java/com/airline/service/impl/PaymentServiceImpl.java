package com.airline.service.impl;

import com.airline.enums.PaymentStatus;
import com.airline.factory.PaymentGatewayFactory;
import com.airline.model.Payment;
import com.airline.repository.PaymentRepository;
import com.airline.service.PaymentService;

import java.util.UUID;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;


    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment processPayment(double amount, String provider) {
        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                amount
        );
        boolean paymentStatus = PaymentGatewayFactory.createGateway(provider).processPayment(payment.getPaymentId(), amount);
        if (paymentStatus) {
            payment.markSuccess();
        } else {
            payment.markFailed();
        }
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public void refund(Payment payment) {
        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);
    }
}