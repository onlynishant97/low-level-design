package com.parkinglot.repository;

import com.parkinglot.enums.TicketStatus;
import com.parkinglot.model.Ticket;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TicketRepository {
    ConcurrentHashMap<String, Ticket> ticketMap;

    public TicketRepository() {
        this.ticketMap = new ConcurrentHashMap<>();
    }

    public void save(Ticket ticket) {
        ticketMap.put(ticket.getTicketId(), ticket);
    }

    public Ticket findById(String ticketId) {
        return ticketMap.get(ticketId);
    }

    public List<Ticket> getAllActiveTickets() {
        return ticketMap.values().stream().filter(ticket -> ticket.getStatus() == TicketStatus.OPEN).toList();
    }

    public Ticket getTicketByVehicleNumber(String vehicleNumber) {
        return ticketMap.values().stream().filter(ticket -> ticket.getVehicle().getLicensePlate().equals(vehicleNumber) && ticket.getStatus() == TicketStatus.OPEN).findFirst().orElse(null);
    }
}
