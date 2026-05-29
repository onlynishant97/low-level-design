package com.airline.model;

public class Refund {

    private final String refundId;
    private final double amount;

    public Refund(String refundId, double amount) {
        this.refundId = refundId;
        this.amount = amount;
    }

    public String getRefundId() {
        return refundId;
    }

    public double getAmount() {
        return amount;
    }
}