package com.carrental.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiclePricing {
    BigDecimal dailyRate;
    int freeKmPerDay;
    BigDecimal extraKmCharge;
}
