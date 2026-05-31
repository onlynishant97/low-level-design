package com.carrental;

import com.carrental.enums.VehicleCategory;
import com.carrental.enums.VehicleStatus;
import com.carrental.model.*;
import com.carrental.repository.*;
import com.carrental.service.*;
import com.carrental.strategy.PricingStrategy;
import com.carrental.strategy.StandardPricingStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Customer customer = new Customer("1", "John Doe", "", "", "");

        System.out.println("Creating Location");
        Location location1 = new Location("1", "Viman Nagar", "xxx", 0, 0);
        Location location2 = new Location("2", "Kalyani Nagar", "xxx", 0, 0);
        Location location3 = new Location("3", "Hinjewadi", "xxx", 0, 0);

        LocationRepository locationRepository = new LocationRepository();
        LocationService locationService = new LocationService(locationRepository);
        locationService.addLocation(location1);
        locationService.addLocation(location2);
        locationService.addLocation(location3);

        System.out.println("Creating Vehicles");
        VehiclePricing vehiclePricing = new VehiclePricing(BigDecimal.valueOf(1000), 100, BigDecimal.valueOf(10));
        Vehicle vehicle1 = new Vehicle("1", "HR20XXXXX", vehiclePricing, VehicleCategory.SUV, VehicleStatus.AVAILABLE, location1);
        Vehicle vehicle2 = new Vehicle("2", "HR20XXXXX", vehiclePricing, VehicleCategory.SUV, VehicleStatus.AVAILABLE, location1);


        VehicleRepository vehicleRepository = new VehicleRepository();
        ReservationRepository reservationRepository = new ReservationRepository();
        VehicleService vehicleService = new VehicleService(vehicleRepository, reservationRepository);
        vehicleService.onboardVehicle(vehicle1);
        vehicleService.onboardVehicle(vehicle2);

        System.out.println("User search for vehicle in Viman Nagar for 5 days");
        vehicleService.searchVehicles(location1, LocalDate.now(), LocalDate.now().plusDays(5)).forEach(v -> System.out.println("Available Vehicle: " + v.getId()));

        System.out.println("User select the vehicle and clicks book now");
        PricingStrategy pricingStrategy = new StandardPricingStrategy();
        PriceComputationService priceComputationService = new PriceComputationService(pricingStrategy);
        BigDecimal computeTotalPrice = priceComputationService.computeTotalPrice(vehicle1, LocalDate.now(), LocalDate.now().plusDays(5));
        System.out.println("Total Price: " + computeTotalPrice);

        System.out.println("User clicks book now,he does payment");
        PaymentService paymentService = new PaymentService(new PaymentRepository());
        ReservationService reservationService = new ReservationService(reservationRepository, vehicleRepository, paymentService, pricingStrategy);
        Reservation reservation1 = reservationService.initiateReservation(customer, vehicle1, LocalDate.now(), LocalDate.now().plusDays(5), computeTotalPrice);
        Payment payment = paymentService.processPayment(computeTotalPrice.doubleValue(), "CC");
        Reservation confirmedReservation = reservationService.createReservation(reservation1.getId(), payment.getPaymentId());
        System.out.println("Reservation Confirmed: " + confirmedReservation.getId());

        System.out.println("User came to pickup car on specified date");
        RentalService rentalService = new RentalService(new RentalRepository());
        Rental pickupVehicle = rentalService.startRental(confirmedReservation, 100);
        Rental returnedVehicle = rentalService.completeRental(pickupVehicle, 150);

        BillingService billingService = new BillingService();
        Bill bill = billingService.generateBill(returnedVehicle, 0);
        System.out.println("Bill Generated: " + bill);
    }
}