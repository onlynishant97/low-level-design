package com.carrental.service;


import com.carrental.enums.ReservationStatus;
import com.carrental.enums.VehicleStatus;
import com.carrental.model.Customer;
import com.carrental.model.Reservation;
import com.carrental.model.Vehicle;
import com.carrental.repository.ReservationRepository;
import com.carrental.repository.VehicleRepository;
import com.carrental.strategy.PricingStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PricingStrategy pricingStrategy;
    private final ConcurrentHashMap<String, ReentrantLock> vehicleLocks = new ConcurrentHashMap<>();

    public ReservationService(ReservationRepository reservationRepository, VehicleRepository vehicleRepository, PaymentService paymentService, PricingStrategy pricingStrategy) {
        this.reservationRepository = reservationRepository;
        this.pricingStrategy = pricingStrategy;
    }

    public Reservation initiateReservation(Customer customer, Vehicle vehicle, LocalDate startDate, LocalDate endDate, BigDecimal computedAmount) {
        // This is more complex, think of different user booking same car for different dates or overlapping date range.
        ReentrantLock lock = getLockForVehicle(vehicle.getId());

        if (lock.tryLock()) {
            try {
                if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
                    throw new IllegalStateException("Vehicle is not available for reservation");
                }

                // Check if there are any existing reservations for the vehicle that overlap with the requested dates
                boolean hasOverlap = reservationRepository.findAll().stream().filter(r -> r.getVehicle().getId().equals(vehicle.getId())).anyMatch(r -> r.getStatus() == ReservationStatus.ACTIVE && (startDate.isBefore(r.getEndDate()) && endDate.isAfter(r.getStartDate())));

                if (hasOverlap) {
                    throw new IllegalStateException("Vehicle is already reserved for the selected dates");
                }

                BigDecimal totalAmount = pricingStrategy.calculate(vehicle, startDate, endDate);

                if (totalAmount.compareTo(computedAmount) != 0) {
                    throw new IllegalStateException("Computed amount does not match the expected amount");
                }


                Reservation reservation = new Reservation(UUID.randomUUID().toString(), customer, vehicle, startDate, endDate, totalAmount, ReservationStatus.INITIATED);

                reservationRepository.save(reservation);
                return reservation;
            } finally {
                lock.unlock();
            }
        }
        throw new IllegalStateException("Could not acquire lock for vehicle reservation");
    }


    public Reservation createReservation(String reservationId, String paymentId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new IllegalStateException("Reservation not found"));
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservationRepository.save(reservation);
        return reservation;
    }

    public void cancelReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.CANCELLED);
        // refund logic to be handled if reservation was ACTIVE

    }

    public void modifyReservation(Reservation reservation, LocalDate newStart, LocalDate newEnd) {
        reservation.setStartDate(newStart);
        reservation.setEndDate(newEnd);
    }

    private ReentrantLock getLockForVehicle(String vehicleId) {
        return vehicleLocks.computeIfAbsent(vehicleId, id -> new ReentrantLock());
    }
}
