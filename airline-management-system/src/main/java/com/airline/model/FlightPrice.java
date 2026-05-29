package com.airline.model;

import com.airline.enums.SeatClass;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FlightPrice {

    private final String id;
    private final String flightId;
    private final SeatClass seatClass;
    private double baseFare;
}
