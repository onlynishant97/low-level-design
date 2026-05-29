package com.airline.repository;

import com.airline.model.SeatInventory;

import java.util.List;
import java.util.Optional;

public interface SeatInventoryRepository {

    void save(SeatInventory inventory);

    Optional<SeatInventory> findByFlightAndSeat(
            String flightId,
            String seatNumber
    );

    List<SeatInventory> findByFlight(String flightId);

    List<SeatInventory> findAvailableSeats(String flightId);
}