package com.airline.repository.impl;

import com.airline.model.Booking;
import com.airline.repository.BookingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBookingRepository implements BookingRepository {

    private final ConcurrentHashMap<String, Booking> bookings =
            new ConcurrentHashMap<>();

    @Override
    public void save(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
    }

    @Override
    public Optional<Booking> findById(String bookingId) {
        return Optional.ofNullable(bookings.get(bookingId));
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }
}