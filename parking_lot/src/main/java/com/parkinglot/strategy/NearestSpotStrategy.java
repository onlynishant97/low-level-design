package com.parkinglot.strategy;

import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.Vehicle;

import java.util.List;

public class NearestSpotStrategy implements SpotAllocationStrategy {

    @Override
    public ParkingSpot findSpot(Vehicle vehicle, List<ParkingFloor> floors) {
        return floors.stream().flatMap(floor -> floor.getSpots().stream()).filter(spot -> spot.isAvailable() && spot.canFit(vehicle)).findFirst().orElse(null);
    }
}