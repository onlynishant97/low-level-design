package com.airline.repository.impl;

import com.airline.model.Flight;
import com.airline.repository.FlightRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFlightRepository implements FlightRepository {

    private final ConcurrentHashMap<String, Flight> flights =
            new ConcurrentHashMap<>();

    @Override
    public void save(Flight flight) {
        flights.put(flight.getFlightId(), flight);
    }

    @Override
    public Optional<Flight> findById(String flightId) {
        return Optional.ofNullable(flights.get(flightId));
    }

    @Override
    public List<Flight> search(String sourceCode,
                               String destinationCode,
                               LocalDate travelDate) {

        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights.values()) {

            boolean sourceMatches = flight.getRoute()
                    .getSource()
                    .getCode()
                    .equalsIgnoreCase(sourceCode);

            boolean destinationMatches = flight.getRoute()
                    .getDestination()
                    .getCode()
                    .equalsIgnoreCase(destinationCode);

            boolean dateMatches = flight.getSchedule()
                    .getDeparture()
                    .toLocalDate()
                    .equals(travelDate);

            if (sourceMatches && destinationMatches && dateMatches) {
                result.add(flight);
            }
        }

        return result;
    }

    @Override
    public List<Flight> findAll() {
        return new ArrayList<>(flights.values());
    }
}