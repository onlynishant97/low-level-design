package com.airline.service;

import com.airline.model.Flight;
import com.airline.model.FlightWithFlightPrice;

import java.time.LocalDate;
import java.util.List;

public interface FlightSearchService {

    List<FlightWithFlightPrice> searchFlights(String source,
                                              String destination,
                                              LocalDate date);
}