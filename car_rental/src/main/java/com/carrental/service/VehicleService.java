package com.carrental.service;

import com.carrental.enums.ReservationStatus;
import com.carrental.model.Location;
import com.carrental.model.Vehicle;
import com.carrental.repository.ReservationRepository;
import com.carrental.repository.VehicleRepository;

import java.time.LocalDate;
import java.util.List;

public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ReservationRepository reservationRepository;

    public VehicleService(VehicleRepository vehicleRepository, ReservationRepository reservationRepository) {
        this.vehicleRepository = vehicleRepository;
        this.reservationRepository = reservationRepository;
    }

    public void onboardVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getVehiclesByLocation(Location location) {
        return vehicleRepository.findAllActiveVehiclesByLocation(location);
    }

    public List<Vehicle> searchVehicles(Location location, LocalDate startDate, LocalDate endDate) {
        List<Vehicle> vehicles = vehicleRepository.findAllActiveVehiclesByLocation(location);
        // Find out all the vehicles are not reserved for the given date range
        return vehicles.stream().filter(v -> reservationRepository.findAll().stream().filter(r -> r.getVehicle().getId().equals(v.getId())).noneMatch(r -> r.getStatus() == ReservationStatus.ACTIVE && (startDate.isBefore(r.getEndDate()) && endDate.isAfter(r.getStartDate())))).toList();
    }
}