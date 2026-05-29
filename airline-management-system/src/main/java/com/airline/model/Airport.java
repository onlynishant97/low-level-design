package com.airline.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Airport {
    private final String code;
    private final String name;
    private final String city;
}