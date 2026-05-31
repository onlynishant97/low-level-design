package com.carrental.model;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Bill {
    private final String id;
    private final Rental rental;
    private final double baseCharge;
    private final double distanceCharge;
    private final double totalCharge;
}