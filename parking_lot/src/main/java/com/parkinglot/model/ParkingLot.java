package com.parkinglot.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParkingLot {
    String id;
    List<ParkingFloor> floors = new ArrayList<>();

    public void addFloor(ParkingFloor floor) {
        this.floors.add(floor);
    }
}
