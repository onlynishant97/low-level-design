package com.airline.repository.impl;

import com.airline.model.Passenger;
import com.airline.repository.PassengerRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPassengerRepository implements PassengerRepository {

    private final ConcurrentHashMap<String, Passenger> passengers =
            new ConcurrentHashMap<>();

    @Override
    public void save(Passenger passenger) {
        passengers.put(passenger.getPassengerId(), passenger);
    }

    @Override
    public Optional<Passenger> findById(String passengerId) {
        return Optional.ofNullable(passengers.get(passengerId));
    }
}