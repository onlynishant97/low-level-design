package com.carrental.service;

import com.carrental.model.Location;
import com.carrental.repository.LocationRepository;

import java.util.List;

public class LocationService {
    LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void addLocation(Location location) {
        locationRepository.save(location);
    }

    public Location getLocation(String locationId) {
        return locationRepository.findById(locationId);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
