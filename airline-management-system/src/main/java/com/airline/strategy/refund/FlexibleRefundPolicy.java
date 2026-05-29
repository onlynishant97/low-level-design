package com.airline.strategy.refund;

public class FlexibleRefundPolicy implements RefundPolicy {

    @Override
    public double calculateRefund(double originalAmount,
                                  long hoursBeforeDeparture) {

        if (hoursBeforeDeparture >= 24) {
            return originalAmount;
        }

        return originalAmount * 0.80;
    }
}