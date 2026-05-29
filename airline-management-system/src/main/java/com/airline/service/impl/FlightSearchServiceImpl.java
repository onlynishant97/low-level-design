package com.airline.service.impl;

import com.airline.model.Flight;
import com.airline.model.FlightPrice;
import com.airline.model.FlightWithFlightPrice;
import com.airline.repository.FlightPriceRepository;
import com.airline.repository.FlightRepository;
import com.airline.service.FlightSearchService;

import java.time.LocalDate;
import java.util.List;

public class FlightSearchServiceImpl implements FlightSearchService {

    private final FlightRepository flightRepository;
    private final FlightPriceRepository flightPriceRepository;

    public FlightSearchServiceImpl(FlightRepository flightRepository, FlightPriceRepository flightPriceRepository) {
        this.flightRepository = flightRepository;
        this.flightPriceRepository = flightPriceRepository;
    }

    @Override
    public List<FlightWithFlightPrice> searchFlights(String source, String destination, LocalDate date) {
        List<Flight> flights = flightRepository.search(source, destination, date);
        return flights.stream().map(flight -> {
            List<FlightPrice> prices = flightPriceRepository.findByFlight(flight.getFlightId());
            return new FlightWithFlightPrice(flight, prices);
        }).toList();
    }
}