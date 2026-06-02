package com.parkinglot.strategy;

import com.parkinglot.model.Ticket;

import java.time.Duration;

public class FlatRateStrategy implements FeeCalculationStrategy {

    private final double hourlyRate;

    public FlatRateStrategy(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double calculateFee(Ticket ticket) {
        long hours = Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toHours();
        hours = Math.max(1, hours);
        return hours * hourlyRate;
    }
}