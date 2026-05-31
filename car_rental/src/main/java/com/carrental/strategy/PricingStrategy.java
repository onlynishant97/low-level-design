package com.carrental.strategy;


import com.carrental.model.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PricingStrategy {
    BigDecimal calculate(
            Vehicle vehicle,
            LocalDate startDate,
            LocalDate endDate
    );
}
