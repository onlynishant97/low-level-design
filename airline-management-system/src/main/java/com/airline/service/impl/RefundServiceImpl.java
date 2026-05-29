package com.airline.service.impl;

import com.airline.model.Booking;
import com.airline.service.PaymentService;
import com.airline.service.RefundService;
import com.airline.strategy.refund.RefundPolicy;

import java.time.Duration;
import java.time.LocalDateTime;

public class RefundServiceImpl implements RefundService {

    private final PaymentService paymentService;
    private final RefundPolicy refundPolicy;

    public RefundServiceImpl(PaymentService paymentService,
                             RefundPolicy refundPolicy) {
        this.paymentService = paymentService;
        this.refundPolicy = refundPolicy;
    }

    @Override
    public void processRefund(Booking booking) {
        if (booking.getPayment() == null) {
            System.out.println("No payment found for booking: "
                    + booking.getBookingId());
            return;
        }

        long hoursBeforeDeparture = Duration.between(
                LocalDateTime.now(),
                booking.getFlight().getSchedule().getDeparture()
        ).toHours();

        double refundAmount = refundPolicy.calculateRefund(
                booking.getPayment().getAmount(),
                hoursBeforeDeparture
        );

        System.out.println("Refund amount calculated: " + refundAmount
                + " for booking: " + booking.getBookingId());

        paymentService.refund(booking.getPayment());
    }
}