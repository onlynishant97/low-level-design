package com.airline.service.impl;

import com.airline.enums.FlightStatus;
import com.airline.model.Flight;
import com.airline.repository.FlightRepository;
import com.airline.service.FlightStatusService;

public class FlightStatusServiceImpl implements FlightStatusService {

    private final FlightRepository flightRepository;

    public FlightStatusServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void updateStatus(String flightId, FlightStatus status) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() ->
                        new RuntimeException("Flight not found: " + flightId));
        flight.setStatus(status);
        flightRepository.save(flight);
    }

    @Override
    public FlightStatus getStatus(String flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() ->
                        new RuntimeException("Flight not found: " + flightId))
                .getStatus();
    }
}