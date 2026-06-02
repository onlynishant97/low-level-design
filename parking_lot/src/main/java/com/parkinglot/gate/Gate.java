package com.parkinglot.gate;

import com.parkinglot.enums.GateType;
import com.parkinglot.service.ParkingLotService;
import lombok.Data;

@Data
public abstract class Gate {
    String gateId;
    GateType type;
    ParkingLotService parkingService;

    public Gate(String gateId, GateType type, ParkingLotService parkingService) {
        this.gateId = gateId;
        this.type = type;
        this.parkingService = parkingService;
    }
}
