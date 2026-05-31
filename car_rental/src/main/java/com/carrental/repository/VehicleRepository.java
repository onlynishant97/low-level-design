package com.carrental.repository;

import com.carrental.enums.VehicleStatus;
import com.carrental.model.Location;
import com.carrental.model.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class VehicleRepository {

    private final Map<String, Vehicle> vehicles = new ConcurrentHashMap<>();
    private final Map<Location, List<Vehicle>> locationVehicleMap = new ConcurrentHashMap<>();

    public void save(Vehicle vehicle) {
        vehicles.put(vehicle.getId(), vehicle);
        locationVehicleMap
                .computeIfAbsent(vehicle.getCurrentLocation(),
                        loc -> new CopyOnWriteArrayList<>())
                .add(vehicle);
    }

    public List<Vehicle> findAllActiveVehiclesByLocation(Location location) {
        return locationVehicleMap.getOrDefault(location, new ArrayList<>()).stream().filter(v -> v.getStatus() == VehicleStatus.AVAILABLE).toList();
    }

}