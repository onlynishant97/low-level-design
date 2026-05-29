package com.airline.strategy.refund;

public class StandardRefundPolicy implements RefundPolicy {

    @Override
    public double calculateRefund(double originalAmount,
                                  long hoursBeforeDeparture) {

        if (hoursBeforeDeparture >= 48) {
            return originalAmount * 0.90;
        }

        if (hoursBeforeDeparture >= 24) {
            return originalAmount * 0.50;
        }

        return 0;
    }
}