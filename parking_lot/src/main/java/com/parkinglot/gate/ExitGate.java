package com.parkinglot.gate;

import com.parkinglot.enums.GateType;
import com.parkinglot.model.Payment;
import com.parkinglot.service.ParkingLotService;

public class ExitGate extends Gate {
    public ExitGate(String id, ParkingLotService parkingService) {
        super(id, GateType.EXIT, parkingService);
    }

    public double calculateFee(String ticketId) {
        return parkingService.calculateParkingFee(ticketId);
    }

    public Payment exit(String ticketId) {
        return parkingService.unparkVehicle(ticketId);
    }
}
