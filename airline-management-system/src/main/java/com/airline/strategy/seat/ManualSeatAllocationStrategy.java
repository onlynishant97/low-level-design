package com.airline.strategy.seat;

import com.airline.enums.SeatStatus;
import com.airline.model.SeatInventory;
import com.airline.repository.SeatInventoryRepository;

public class ManualSeatAllocationStrategy implements SeatAllocationStrategy {
    private final SeatInventoryRepository seatInventoryRepository;

    public ManualSeatAllocationStrategy(SeatInventoryRepository seatInventoryRepository) {
        this.seatInventoryRepository = seatInventoryRepository;
    }

    @Override
    public SeatInventory lockSeat(
            SeatInventory seatInventory,
            String bookingId) {

        if (seatInventory.getStatus() != SeatStatus.AVAILABLE) {
            throw new RuntimeException(
                    "Seat not available: " + seatInventory.getSeatNumber()
            );
        }

        seatInventory.setStatus(SeatStatus.LOCKED);
        seatInventory.setBookingId(bookingId);
        seatInventory.setVersion(seatInventory.getVersion() + 1);
        seatInventoryRepository.save(seatInventory);

        return seatInventory;
    }

    @Override
    public SeatInventory bookSeat(
            SeatInventory seatInventory,
            String bookingId) {

        if (seatInventory.getStatus() != SeatStatus.LOCKED) {
            throw new RuntimeException(
                    "Seat not locked: " + seatInventory.getSeatNumber()
            );
        }

        if (!bookingId.equals(seatInventory.getBookingId())) {
            throw new RuntimeException("Invalid booking ownership");
        }

        seatInventory.setStatus(SeatStatus.BOOKED);
        seatInventory.setVersion(seatInventory.getVersion() + 1);
        seatInventoryRepository.save(seatInventory);
        return seatInventory;
    }

    @Override
    public SeatInventory unlockSeat(SeatInventory seatInventory) {

        if (seatInventory.getStatus() == SeatStatus.LOCKED) {
            seatInventory.setStatus(SeatStatus.AVAILABLE);
            seatInventory.setBookingId(null);
            seatInventory.setVersion(seatInventory.getVersion() + 1);
            seatInventoryRepository.save(seatInventory);
        }

        return seatInventory;
    }
}