package com.airline.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Passenger {
    private final String passengerId;
    private final String name;
    private final String passport;
}