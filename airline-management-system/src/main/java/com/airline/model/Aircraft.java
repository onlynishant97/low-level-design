package com.airline.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Aircraft {
    private final String aircraftId;
    private final String model;
    private final int capacity;
}