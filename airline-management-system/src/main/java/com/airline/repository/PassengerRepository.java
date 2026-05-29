package com.airline.repository;

import com.airline.model.Passenger;

import java.util.Optional;

public interface PassengerRepository {

    void save(Passenger passenger);

    Optional<Passenger> findById(String passengerId);
}