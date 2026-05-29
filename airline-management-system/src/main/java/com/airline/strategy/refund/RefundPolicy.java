package com.airline.strategy.refund;

public interface RefundPolicy {

    double calculateRefund(double originalAmount, long hoursBeforeDeparture);
}