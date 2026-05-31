package com.carrental.repository;

import com.carrental.model.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocationRepository {
    ConcurrentHashMap<String, Location> locationMap = new ConcurrentHashMap<>();

    public void save(Location location) {
        locationMap.put(location.getId(), location);
    }

    public Location findById(String locationId) {
        return locationMap.get(locationId);
    }

    public List<Location> findAll() {
        return new ArrayList<>(locationMap.values());
    }
}
