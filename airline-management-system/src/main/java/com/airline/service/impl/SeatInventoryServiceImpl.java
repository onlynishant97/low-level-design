package com.airline.service.impl;

import com.airline.enums.SeatStatus;
import com.airline.model.SeatInventory;
import com.airline.model.SeatLock;
import com.airline.repository.SeatInventoryRepository;
import com.airline.service.SeatInventoryService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SeatInventoryServiceImpl
        implements SeatInventoryService {

    private static final int LOCK_DURATION_MINUTES = 5;

    private final Map<String, SeatLock> locks =
            new ConcurrentHashMap<>();


    private final SeatInventoryRepository repository;

    public SeatInventoryServiceImpl(
            SeatInventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void lockSeat(
            String flightId,
            String seatNumber,
            String bookingId) {

        SeatInventory inventory =
                repository.findByFlightAndSeat(
                        flightId,
                        seatNumber
                ).orElseThrow();

        if (inventory.getStatus() != SeatStatus.AVAILABLE) {
            throw new RuntimeException("Seat unavailable");
        }

        if (!acquireLock(
                flightId,
                seatNumber,
                bookingId)) {
            throw new RuntimeException("Seat lock failed");
        }

        inventory.setStatus(SeatStatus.LOCKED);
        repository.save(inventory);
    }

    @Override
    public void confirmSeat(
            String flightId,
            String seatNumber,
            String bookingId) {

        if (!validateLockOwner(
                flightId,
                seatNumber,
                bookingId)) {
            throw new RuntimeException("Invalid seat lock");
        }

        SeatInventory inventory =
                repository.findByFlightAndSeat(
                        flightId,
                        seatNumber
                ).orElseThrow();

        inventory.setStatus(SeatStatus.BOOKED);
        inventory.setBookingId(bookingId);
        inventory.setVersion(
                inventory.getVersion() + 1
        );

        repository.save(inventory);

        releaseLock(
                flightId,
                seatNumber
        );
    }

    @Override
    public void releaseSeat(
            String flightId,
            String seatNumber) {

        SeatInventory inventory =
                repository.findByFlightAndSeat(
                        flightId,
                        seatNumber
                ).orElseThrow();

        inventory.setStatus(SeatStatus.AVAILABLE);

        repository.save(inventory);

        releaseLock(
                flightId,
                seatNumber
        );
    }

    public boolean acquireLock(
            String flightId,
            String seatNumber,
            String bookingId) {
        String key = key(flightId, seatNumber);
        boolean[] acquired = {false};

        locks.compute(key, (k, existing) -> {
            if (existing != null && !existing.isExpired()) {
                acquired[0] = false;
                return existing;
            }
            acquired[0] = true;
            return new SeatLock(
                    flightId, seatNumber, bookingId,
                    LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES)
            );
        });

        return acquired[0];
    }

    private String key(String flightId, String seatNumber) {
        return flightId + ":" + seatNumber;
    }

    public void releaseLock(
            String flightId,
            String seatNumber) {

        locks.remove(key(flightId, seatNumber));
    }

    public boolean validateLockOwner(
            String flightId,
            String seatNumber,
            String bookingId) {

        SeatLock lock = locks.get(key(flightId, seatNumber));

        return lock != null &&
                !lock.isExpired() &&
                lock.getBookingId().equals(bookingId);
    }
}