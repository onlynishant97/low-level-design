package com.parkinglot.model;

import lombok.Data;

import java.util.List;

@Data
public class ParkingFloor {
    int floorNumber;
    List<ParkingSpot> spots;


    public ParkingFloor(int floorNumber, List<ParkingSpot> spots) {
        this.floorNumber = floorNumber;
        this.spots = spots;
    }

    public void addSpot(ParkingSpot spot) {
        this.spots.add(spot);
    }

}
