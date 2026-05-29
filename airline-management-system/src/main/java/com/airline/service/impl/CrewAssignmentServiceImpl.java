package com.airline.service.impl;

import com.airline.model.Crew;
import com.airline.model.Flight;
import com.airline.service.CrewAssignmentService;

import java.util.List;

public class CrewAssignmentServiceImpl
        implements CrewAssignmentService {

    @Override
    public void assignCrew(Flight flight, List<Crew> crew) {
        flight.setCrew(crew);
    }
}