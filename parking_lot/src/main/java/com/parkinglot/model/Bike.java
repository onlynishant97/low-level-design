package com.parkinglot.model;

import com.parkinglot.enums.VehicleSize;
import com.parkinglot.enums.VehicleType;

public class Bike extends Vehicle {

    public Bike(String plateNumber) {
        super(plateNumber, VehicleType.BIKE, VehicleSize.SMALL);
    }
}
