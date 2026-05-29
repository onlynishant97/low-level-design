package com.airline.repository;

import com.airline.model.Flight;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightRepository {

    void save(Flight flight);

    Optional<Flight> findById(String flightId);

    List<Flight> search(String sourceCode,
                        String destinationCode,
                        LocalDate travelDate);

    List<Flight> findAll();
}