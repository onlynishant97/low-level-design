package com.amazonlocker.model;

import lombok.Data;

import java.util.List;

@Data
public class LockerLocation {
    private final String locationId;
    private final String name;
    private final List<Locker> lockers;

    public LockerLocation(String locationId, String name, List<Locker> lockers) {
        this.locationId = locationId;
        this.name = name;
        this.lockers = lockers;
    }

    
}
