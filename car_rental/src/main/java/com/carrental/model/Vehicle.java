package com.carrental.model;

import com.carrental.enums.VehicleCategory;
import com.carrental.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Vehicle {
    String id;
    String registrationNumber;
    VehiclePricing pricing;
    VehicleCategory category;
    VehicleStatus status;
    Location currentLocation;
}
