package com.parkinglot.gate;

import com.parkinglot.enums.GateType;
import com.parkinglot.model.Ticket;
import com.parkinglot.model.Vehicle;
import com.parkinglot.service.ParkingLotService;

public class EntryGate extends Gate {
    public EntryGate(String id, ParkingLotService parkingService) {
        super(id, GateType.ENTRY, parkingService);
    }

    public Ticket generateTicket(Vehicle vehicle) {
      return  parkingService.parkVehicle(vehicle);
    }
}
