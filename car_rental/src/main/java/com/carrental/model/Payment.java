package com.carrental.model;

import com.carrental.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Payment {
    private final String paymentId;
    private final double amount;
    private PaymentStatus status;

    public Payment(String paymentId, double amount) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }


    public void markSuccess() {
        status = PaymentStatus.SUCCESS;
    }

    public void markFailed() {
        status = PaymentStatus.FAILED;
    }

    public void markRefunded() {
        status = PaymentStatus.REFUNDED;
    }
}