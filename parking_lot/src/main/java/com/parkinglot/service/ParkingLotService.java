package com.parkinglot.service;

import com.parkinglot.enums.TicketStatus;
import com.parkinglot.gate.EntryGate;
import com.parkinglot.gate.ExitGate;
import com.parkinglot.model.*;
import com.parkinglot.repository.SpotRepository;
import com.parkinglot.repository.TicketRepository;
import com.parkinglot.strategy.FeeCalculationStrategy;
import com.parkinglot.strategy.SpotAllocationStrategy;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class ParkingLotService {
    private final ParkingLot parkingLot;
    private final TicketRepository ticketRepository;
    private final SpotRepository spotRepository;
    List<EntryGate> entryGates;
    List<ExitGate> exitGates;
    private volatile SpotAllocationStrategy allocationStrategy;
    private volatile FeeCalculationStrategy feeStrategy;
    private ConcurrentHashMap<String, ReentrantLock> spotLocks;

    public ParkingLotService(ParkingLot parkingLot, TicketRepository ticketRepository, SpotRepository spotRepository) {
        this.parkingLot = parkingLot;
        this.ticketRepository = ticketRepository;
        this.spotRepository = spotRepository;
        this.spotLocks = new ConcurrentHashMap<>();
    }


    public Ticket parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = allocationStrategy.findSpot(vehicle, parkingLot.getFloors());
        if (spot == null) {
            throw new RuntimeException("No spot available");
        }
        ReentrantLock lock = spotLocks.computeIfAbsent(spot.getId(), k -> new ReentrantLock());
        lock.lock();
        try {
            if (!spot.isAvailable()) {
                throw new RuntimeException("Spot just got occupied, try again");
            }
            spot.occupy(vehicle);
            Ticket ticket = new Ticket(UUID.randomUUID().toString(), vehicle, spot);
            ticketRepository.save(ticket);
            return ticket;
        } finally {
            lock.unlock();
        }
    }

    public double calculateParkingFee(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Invalid ticket");
        }
        ticket.setExitTime(LocalDateTime.now());
        return feeStrategy.calculateFee(ticket);
    }

    public Payment unparkVehicle(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Invalid ticket");
        }
        ParkingSpot spot = ticket.getAssignedSpot();
        ReentrantLock lock = spotLocks.computeIfAbsent(spot.getId(), k -> new ReentrantLock());
        lock.lock();
        try {
            spot.vacate();
            ticket.setExitTime(LocalDateTime.now());
            double fee = feeStrategy.calculateFee(ticket);
            ticket.close(new Payment(fee));
            ticketRepository.save(ticket);
            return new Payment(fee);
        } finally {
            lock.unlock();
        }
    }

    public boolean isVehicleParked(String vehicleNumber) {
        Ticket ticket = ticketRepository.getTicketByVehicleNumber(vehicleNumber);
        return ticket != null && ticket.getStatus() == TicketStatus.OPEN;
    }

    void addFloor(ParkingFloor floor) {
        parkingLot.getFloors().add(floor);
    }

    void addEntryGate(EntryGate gate) {
        entryGates.add(gate);
    }

    void addExitGate(ExitGate gate) {
        exitGates.add(gate);
    }

}
