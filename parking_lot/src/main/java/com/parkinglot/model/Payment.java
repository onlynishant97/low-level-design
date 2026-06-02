package com.parkinglot.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Payment {
    String paymentId;
    double amount;
    LocalDateTime paidAt;

    public Payment(double amount) {
        this.paymentId = UUID.randomUUID().toString();
        this.amount = amount;
        this.paidAt = LocalDateTime.now();
    }
}
