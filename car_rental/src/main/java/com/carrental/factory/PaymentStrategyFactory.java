package com.carrental.factory;

import com.carrental.strategy.CreditCardPaymentStrategy;
import com.carrental.strategy.PaymentStrategy;

public final class PaymentStrategyFactory {
    private PaymentStrategyFactory() {
    }

    public static PaymentStrategy getPaymentStrategy(String paymentMethod) {
        switch (paymentMethod.toLowerCase()) {
            case "cc":
                return new CreditCardPaymentStrategy();
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        }
    }
}
