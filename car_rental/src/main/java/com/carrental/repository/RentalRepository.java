package com.carrental.repository;

import com.carrental.model.Rental;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class RentalRepository {

    private final ConcurrentHashMap<String, Rental> rentals = new ConcurrentHashMap<>();

    public void save(Rental rental) {
        rentals.put(rental.getId(), rental);
    }

    public Optional<Rental> findById(String rentalId) {
        return rentals.values().stream().filter(r -> r.getReservation().getId().equals(rentalId)).findFirst();
    }

    public Optional<Rental> findByReservationId(String reservationId) {
        return Optional.ofNullable(rentals.get(reservationId));
    }

    public List<Rental> findAll() {
        return new ArrayList<>(rentals.values());
    }
}