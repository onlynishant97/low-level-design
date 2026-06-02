package com.parkinglot;

import com.parkinglot.enums.*;
import com.parkinglot.gate.EntryGate;
import com.parkinglot.gate.ExitGate;
import com.parkinglot.model.*;
import com.parkinglot.repository.SpotRepository;
import com.parkinglot.repository.TicketRepository;
import com.parkinglot.service.ParkingLotService;
import com.parkinglot.strategy.*;

import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        NearestSpotStrategy allocationStrategy = new NearestSpotStrategy();

        FlatRateStrategy feeStrategy = new FlatRateStrategy(50);

        ParkingSpot spot1 = new ParkingSpot(1, 1, VehicleSize.MEDIUM);
        ParkingSpot spot2 = new ParkingSpot(2, 1, VehicleSize.MEDIUM);

        ParkingFloor parkingFloor = new ParkingFloor(1, new ArrayList<>(List.of(spot1, spot2)));
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.addFloor(parkingFloor);

        TicketRepository ticketRepository = new TicketRepository();
        SpotRepository spotRepository = new SpotRepository();
        ParkingLotService parkingLotService = new ParkingLotService(parkingLot, ticketRepository, spotRepository);
        parkingLotService.setAllocationStrategy(allocationStrategy);
        parkingLotService.setFeeStrategy(feeStrategy);
        EntryGate entryGate = new EntryGate("1", parkingLotService);
        ExitGate exitGate = new ExitGate("1", parkingLotService);


        Vehicle car = new Car("MH12-CAR-2222");
        Ticket carTicket = entryGate.generateTicket(car);
        System.out.println("Car parked at spot: " + carTicket);

        System.out.println("Parking fee for car: " + exitGate.calculateFee(carTicket.getTicketId()));

        Payment payment = exitGate.exit(carTicket.getTicketId());
        System.out.println("Payment successful: " + payment);
    }
}