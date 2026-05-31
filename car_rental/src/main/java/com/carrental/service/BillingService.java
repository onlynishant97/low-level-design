package com.carrental.service;

import com.carrental.model.Bill;
import com.carrental.model.Rental;
import com.carrental.model.VehiclePricing;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class BillingService {


    public Bill generateBill(Rental rental, double damageFee) {
        double baseAmount = rental.getReservation().getEstimatedCost().doubleValue();

        int totalKm = rental.getEndKm() - rental.getStartKm();
        int rentalDays = (int) ChronoUnit.DAYS.between(
                rental.getReservation().getStartDate(),
                rental.getReservation().getEndDate()
        );
        VehiclePricing pricing = rental.getReservation().getVehicle().getPricing();
        int freeKm = pricing.getFreeKmPerDay() * rentalDays;
        int excessKm = Math.max(0, totalKm - freeKm);
        double excessKmCharge = excessKm * pricing.getExtraKmCharge().doubleValue();

        double total = baseAmount + excessKmCharge + damageFee;
        return new Bill(UUID.randomUUID().toString(), rental, baseAmount, excessKmCharge, total);
    }
}