package com.airline.repository;

import com.airline.enums.SeatClass;
import com.airline.model.FlightPrice;

import java.util.*;

public interface FlightPriceRepository {

    void save(FlightPrice flightPrice);

    Optional<FlightPrice> findByFlightAndSeatClass(String flightId, SeatClass seatClass);

    List<FlightPrice> findByFlight(String flightId);
}