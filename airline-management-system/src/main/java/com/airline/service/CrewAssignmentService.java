package com.airline.service;

import com.airline.model.Crew;
import com.airline.model.Flight;

import java.util.List;

public interface CrewAssignmentService {

    void assignCrew(Flight flight, List<Crew> crew);
}