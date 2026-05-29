package com.airline.service;

import com.airline.enums.FlightStatus;

public interface FlightStatusService {
    void updateStatus(String flightId, FlightStatus status);
    FlightStatus getStatus(String flightId);
}