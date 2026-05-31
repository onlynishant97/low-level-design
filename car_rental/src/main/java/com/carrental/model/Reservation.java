package com.carrental.model;

import com.carrental.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Reservation {
    String id;
    Customer customer;
    Vehicle vehicle;
    LocalDate startDate;
    LocalDate endDate;
    BigDecimal estimatedCost;
    ReservationStatus status;
}
