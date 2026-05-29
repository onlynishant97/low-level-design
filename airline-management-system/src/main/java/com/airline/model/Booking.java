package com.airline.model;

import com.airline.enums.BookingStatus;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Booking {

    private final String bookingId;
    private final Flight flight;
    private final List<Passenger> passengers;
    private final List<Seat> seats;
    private final double bookingAmount;
    private Payment payment;
    private BookingStatus status;
    private Map<String, String> passengerSeatMap;


    public Booking(String bookingId,
                   Flight flight,
                   List<Passenger> passengers,
                   List<Seat> seats,
                   double bookingAmount) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.passengers = passengers;
        this.seats = seats;
        this.status = BookingStatus.INITIATED;
        this.bookingAmount = bookingAmount;
    }


}