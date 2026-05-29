package com.airline.service.impl;

import com.airline.model.Aircraft;
import com.airline.model.Flight;
import com.airline.service.AircraftAssignmentService;

public class AircraftAssignmentServiceImpl
        implements AircraftAssignmentService {

    @Override
    public void assignAircraft(Flight flight, Aircraft aircraft) {
        flight.setAircraft(aircraft);
    }
}