package com.carrental.repository;

import com.carrental.model.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ReservationRepository {

    private final ConcurrentHashMap<String, Reservation> reservations =
            new ConcurrentHashMap<>();

    public void save(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
    }

    public Optional<Reservation> findById(String reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    public List<Reservation> findByVehicle(String vehicleId) {
        List<Reservation> result = new ArrayList<>();

        for (Reservation reservation : reservations.values()) {
            if (reservation.getVehicle().getId().equals(vehicleId)) {
                result.add(reservation);
            }
        }

        return result;
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    public void delete(String reservationId) {
        reservations.remove(reservationId);
    }
}