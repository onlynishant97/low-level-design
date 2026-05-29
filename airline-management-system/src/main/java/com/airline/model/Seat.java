package com.airline.model;

import com.airline.enums.SeatClass;
import com.airline.enums.SeatStatus;
import lombok.Data;

@Data
public class Seat {

    private final String seatNumber;
    private final SeatClass seatClass;


    public Seat(String seatNumber, SeatClass seatClass) {
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
    }
}