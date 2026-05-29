package com.airline.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Baggage {
    private final int count;
    private final double weight;
    private final String passengerId;
    private final String flightId;
}