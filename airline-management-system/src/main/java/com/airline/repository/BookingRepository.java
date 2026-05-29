package com.airline.repository;

import com.airline.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    void save(Booking booking);

    Optional<Booking> findById(String bookingId);

    List<Booking> findAll();
}