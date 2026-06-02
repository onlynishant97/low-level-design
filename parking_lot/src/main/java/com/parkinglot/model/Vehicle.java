package com.parkinglot.model;

import com.parkinglot.enums.VehicleSize;
import com.parkinglot.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Vehicle {
    String licensePlate;
    VehicleType type;
    VehicleSize size;
}
