package com.carrental.strategy;


import com.carrental.model.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StandardPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculate(
            Vehicle vehicle,
            LocalDate startDate,
            LocalDate endDate) {

        int days = (int) ChronoUnit.DAYS.between(startDate, endDate);

        BigDecimal baseRate = vehicle.getPricing().getDailyRate();
        BigDecimal multiplier = BigDecimal.ONE;

        return baseRate
                .multiply(BigDecimal.valueOf(days))
                .multiply(multiplier);

    }
}
