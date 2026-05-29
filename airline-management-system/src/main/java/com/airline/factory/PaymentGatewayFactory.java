package com.airline.factory;

import com.airline.strategy.payment.CardPaymentGateway;
import com.airline.strategy.payment.PaymentGateway;
import com.airline.strategy.payment.UpiPaymentGateway;

public final class PaymentGatewayFactory {

    private PaymentGatewayFactory() {
    }

    public static PaymentGateway createGateway(String type) {
        return switch (type.toUpperCase()) {
            case "CARD" -> new CardPaymentGateway();
            case "UPI" -> new UpiPaymentGateway();
            default -> throw new IllegalArgumentException(
                    "Unsupported payment gateway: " + type
            );
        };
    }
}