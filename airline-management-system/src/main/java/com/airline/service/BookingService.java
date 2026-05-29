package com.airline.service;

import com.airline.model.Booking;
import com.airline.model.Passenger;
import com.airline.model.Seat;

import java.util.List;

public interface BookingService {

    Booking initiateBooking(String flightId,
                            double flightPrice,
                            List<Seat> seats,
                            List<Passenger> passengers);


    Booking completeBooking(String bookingId,
                            String paymentId);

    void cancelBooking(String bookingId);
}