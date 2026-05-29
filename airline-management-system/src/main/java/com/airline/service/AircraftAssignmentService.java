package com.airline.service;

import com.airline.model.Aircraft;
import com.airline.model.Flight;

public interface AircraftAssignmentService {

    void assignAircraft(Flight flight, Aircraft aircraft);
}