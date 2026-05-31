package com.carrental.model;

import com.carrental.enums.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Rental {
    String id;
    Reservation reservation;
    LocalDateTime startTime;
    LocalDateTime endTime;
    RentalStatus status;
    int startKm;
    int endKm;
}
