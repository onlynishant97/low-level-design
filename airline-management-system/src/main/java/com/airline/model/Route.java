package com.airline.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Route {

    private final Airport source;
    private final Airport destination;
}