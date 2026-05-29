package com.airline.repository.impl;

import com.airline.enums.SeatStatus;
import com.airline.model.SeatInventory;
import com.airline.repository.SeatInventoryRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemorySeatInventoryRepository
        implements SeatInventoryRepository {

    private final Map<String, SeatInventory> storage =
            new ConcurrentHashMap<>();

    private String key(String flightId, String seatNumber) {
        return flightId + ":" + seatNumber;
    }

    @Override
    public void save(SeatInventory inventory) {
        storage.put(
                key(inventory.getFlightId(),
                        inventory.getSeatNumber()),
                inventory
        );
    }

    @Override
    public Optional<SeatInventory> findByFlightAndSeat(
            String flightId,
            String seatNumber) {

        return Optional.ofNullable(
                storage.get(key(flightId, seatNumber))
        );
    }

    @Override
    public List<SeatInventory> findByFlight(String flightId) {
        return storage.values().stream()
                .filter(i -> i.getFlightId().equals(flightId))
                .collect(Collectors.toList());
    }

    @Override
    public List<SeatInventory> findAvailableSeats(
            String flightId) {

        return storage.values().stream()
                .filter(i ->
                        i.getFlightId().equals(flightId) &&
                                i.getStatus() == SeatStatus.AVAILABLE)
                .collect(Collectors.toList());
    }
}