package com.airline.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FlightWithFlightPrice {
    Flight flight;
    List<FlightPrice> price;
}
