package com.parkinglot.model;

import com.parkinglot.enums.SpotStatus;
import com.parkinglot.enums.VehicleSize;
import lombok.Data;

import java.util.UUID;

@Data
public class ParkingSpot {
    String id;
    int spotNumber;
    int floor;
    SpotStatus status;
    VehicleSize size;
    Vehicle currentVehicle;

    public ParkingSpot(int spotNumber, int floor, VehicleSize size) {
        this.id = UUID.randomUUID().toString();
        this.spotNumber = spotNumber;
        this.floor = floor;
        this.size = size;
        this.status = SpotStatus.AVAILABLE;
    }

    public boolean canFit(Vehicle vehicle) {
        VehicleSize vehicleSize = vehicle.getSize();
        return switch (size) {
            case SMALL -> vehicleSize == VehicleSize.SMALL;
            case MEDIUM -> vehicleSize == VehicleSize.SMALL || vehicleSize == VehicleSize.MEDIUM;
            case LARGE -> true;
        };
    }

    public void occupy(Vehicle vehicle) {
        this.currentVehicle = vehicle;
        this.status = SpotStatus.OCCUPIED;
    }

    public void vacate() {
        this.currentVehicle = null;
        this.status = SpotStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        return this.status == SpotStatus.AVAILABLE;
    }

}
