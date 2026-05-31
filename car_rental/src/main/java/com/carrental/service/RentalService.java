package com.carrental.service;

import com.carrental.enums.RentalStatus;
import com.carrental.enums.ReservationStatus;
import com.carrental.model.Rental;
import com.carrental.model.Reservation;
import com.carrental.repository.RentalRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public Rental startRental(Reservation reservation, int startKm) {
        Rental rental = Rental.builder().id(UUID.randomUUID().toString()).reservation(reservation).startTime(LocalDateTime.now()).startKm(startKm).status(RentalStatus.ACTIVE).build();
        rentalRepository.save(rental);
        return rental;
    }

    public Rental completeRental(Rental rental, int endKM) {
        rental.setEndKm(endKM);
        rental.setStatus(RentalStatus.COMPLETED);
        rental.setEndTime(LocalDateTime.now());
        rental.getReservation().setStatus(ReservationStatus.COMPLETED);
        rentalRepository.save(rental);
        return rental;
    }
}