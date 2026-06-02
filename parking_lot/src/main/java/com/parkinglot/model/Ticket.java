package com.parkinglot.model;

import com.parkinglot.enums.TicketStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Ticket {
    String ticketId;
    Vehicle vehicle;
    ParkingSpot assignedSpot;
    LocalDateTime entryTime;
    LocalDateTime exitTime;
    TicketStatus status;
    Payment payment;

    public Ticket(String ticketId, Vehicle vehicle, ParkingSpot assignedSpot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.assignedSpot = assignedSpot;
        this.entryTime = LocalDateTime.now();
        this.status = TicketStatus.OPEN;
    }

    public void close(Payment payment) {
        this.payment = payment;
        this.status = TicketStatus.CLOSED;
    }
}
