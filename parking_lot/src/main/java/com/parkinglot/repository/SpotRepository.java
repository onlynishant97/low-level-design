package com.parkinglot.repository;

import com.parkinglot.model.ParkingSpot;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SpotRepository {
    ConcurrentHashMap<String, ParkingSpot> spotMap = new ConcurrentHashMap<>();

    public List<ParkingSpot> getAllSpots() {
        return (List<ParkingSpot>) spotMap.values();
    }

    public List<ParkingSpot> getAllAvailableSpots() {
        return spotMap.values()
                .stream().filter(ParkingSpot::isAvailable).toList();
    }

    public void addSpot(ParkingSpot spot) {
        spotMap.put(spot.getId(), spot);
    }
}
