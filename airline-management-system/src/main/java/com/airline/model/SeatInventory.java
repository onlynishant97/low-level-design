package com.airline.model;

import com.airline.enums.SeatClass;
import com.airline.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatInventory {
    private String inventoryId;
    private String flightId;
    private String seatNumber;
    private SeatClass seatClass;
    private SeatStatus status;
    private String bookingId;
    private int version;
}
