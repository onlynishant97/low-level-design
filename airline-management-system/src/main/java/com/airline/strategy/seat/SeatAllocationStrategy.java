package com.airline.strategy.seat;

import com.airline.model.SeatInventory;

public interface SeatAllocationStrategy {

    SeatInventory lockSeat(SeatInventory seatInventory, String bookingId);

    SeatInventory bookSeat(SeatInventory seatInventory, String bookingId);

    SeatInventory unlockSeat(SeatInventory seatInventory);
}