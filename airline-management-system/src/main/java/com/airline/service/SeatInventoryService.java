package com.airline.service;

public interface SeatInventoryService {

    void lockSeat(
            String flightId,
            String seatNumber,
            String bookingId
    );

    void confirmSeat(
            String flightId,
            String seatNumber,
            String bookingId
    );

    void releaseSeat(
            String flightId,
            String seatNumber
    );
}