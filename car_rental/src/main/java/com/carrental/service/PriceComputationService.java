package com.carrental.service;

import com.carrental.model.Vehicle;
import com.carrental.strategy.PricingStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PriceComputationService {
    PricingStrategy pricingStrategy;

    public PriceComputationService(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public BigDecimal computeTotalPrice(Vehicle vehicle, LocalDate startDate, LocalDate endDate) {
        return pricingStrategy.calculate(vehicle,
                startDate,
                endDate
        );
    }

}
