package com.airline.service.impl;

import com.airline.enums.BookingStatus;
import com.airline.enums.PaymentStatus;
import com.airline.model.*;
import com.airline.repository.BookingRepository;
import com.airline.repository.FlightRepository;
import com.airline.repository.PaymentRepository;
import com.airline.repository.SeatInventoryRepository;
import com.airline.service.BookingService;
import com.airline.service.RefundService;
import com.airline.strategy.seat.SeatAllocationStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BookingServiceImpl implements BookingService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final RefundService refundService;
    private final PaymentRepository paymentRepository;
    private final SeatInventoryRepository seatInventoryRepository;
    private final SeatAllocationStrategy seatAllocationStrategy;

    // Seat-level locks for fine-grained concurrency
    private final ConcurrentHashMap<String, Object> seatLockObjects =
            new ConcurrentHashMap<>();

    public BookingServiceImpl(FlightRepository flightRepository,
                              BookingRepository bookingRepository,
                              RefundService refundService,
                              PaymentRepository paymentRepository,
                              SeatAllocationStrategy seatAllocationStrategy,
                              SeatInventoryRepository seatInventoryRepository) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.refundService = refundService;
        this.paymentRepository = paymentRepository;
        this.seatAllocationStrategy = seatAllocationStrategy;
        this.seatInventoryRepository = seatInventoryRepository;
    }

    private Object getSeatLock(String flightId, String seatNumber) {
        String key = flightId + ":" + seatNumber;
        seatLockObjects.putIfAbsent(key, new Object());
        return seatLockObjects.get(key);
    }

    @Override
    public Booking initiateBooking(String flightId, double flightPrice,
                                   List<Seat> seats,
                                   List<Passenger> passengers) {

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() ->
                        new RuntimeException("Flight not found"));

        String bookingId = UUID.randomUUID().toString();
        List<Seat> lockedSeats = new ArrayList<>();

        try {
            for (Seat seat : seats) {
                synchronized (getSeatLock(flightId, seat.getSeatNumber())) {
                    SeatInventory inventory = seatInventoryRepository
                            .findByFlightAndSeat(flightId, seat.getSeatNumber())
                            .orElseThrow(() ->
                                    new RuntimeException("Seat inventory not found"));

                    seatAllocationStrategy.lockSeat(inventory, bookingId);
                    lockedSeats.add(seat);
                }
            }
        } catch (Exception e) {
            // Rollback all locked seats
            for (Seat locked : lockedSeats) {
                synchronized (getSeatLock(flightId, locked.getSeatNumber())) {
                    seatInventoryRepository
                            .findByFlightAndSeat(flightId, locked.getSeatNumber())
                            .ifPresent(seatAllocationStrategy::unlockSeat);
                }
            }
            throw e;
        }

        Booking booking = new Booking(bookingId, flight,
                passengers, seats, flightPrice);
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Booking completeBooking(String bookingId, String paymentId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.INITIATED) {
            throw new IllegalStateException(
                    "Booking cannot be completed in status: "
                            + booking.getStatus());
        }

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            booking.setStatus(BookingStatus.FAILED);
            bookingRepository.save(booking);
            // Release seats back
            for (Seat seat : booking.getSeats()) {
                seatInventoryRepository
                        .findByFlightAndSeat(
                                booking.getFlight().getFlightId(),
                                seat.getSeatNumber())
                        .ifPresent(seatAllocationStrategy::unlockSeat);
            }

            throw new RuntimeException("Payment not successful");
        }

        booking.setPayment(payment);
        booking.setStatus(BookingStatus.CONFIRMED);

        for (Seat seat : booking.getSeats()) {
            synchronized (getSeatLock(
                    booking.getFlight().getFlightId(),
                    seat.getSeatNumber())) {

                SeatInventory inventory = seatInventoryRepository
                        .findByFlightAndSeat(
                                booking.getFlight().getFlightId(),
                                seat.getSeatNumber())
                        .orElseThrow();

                seatAllocationStrategy.bookSeat(inventory, bookingId);
            }
        }

        bookingRepository.save(booking);

        return booking;
    }

    @Override
    public void cancelBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        for (Seat seat : booking.getSeats()) {
            synchronized (getSeatLock(
                    booking.getFlight().getFlightId(),
                    seat.getSeatNumber())) {

                seatInventoryRepository
                        .findByFlightAndSeat(
                                booking.getFlight().getFlightId(),
                                seat.getSeatNumber())
                        .ifPresent(seatAllocationStrategy::unlockSeat);
            }
        }

        refundService.processRefund(booking);
        bookingRepository.save(booking);
    }
}