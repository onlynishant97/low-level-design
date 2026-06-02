package com.parkinglot.factory;

import com.parkinglot.enums.VehicleType;
import com.parkinglot.model.Bike;
import com.parkinglot.model.Car;
import com.parkinglot.model.Vehicle;

public class VehicleFactory {
    public static Vehicle createVehicle(VehicleType type, String licensePlate) {
        return switch (type) {
            case CAR -> new Car(licensePlate);
            case BIKE -> new Bike(licensePlate);
        };
    }
}
