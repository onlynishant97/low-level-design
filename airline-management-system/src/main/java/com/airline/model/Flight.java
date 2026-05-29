package com.airline.model;

import com.airline.enums.FlightStatus;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Flight {
    private String flightId;
    private Route route;
    private Schedule schedule;
    private Aircraft aircraft;
    private Map<String, Seat> seats;
    private List<Crew> crew;
    private FlightStatus status;



    public Flight(String flightId,
                  Route route,
                  Schedule schedule,
                  Aircraft aircraft,
                  Map<String, Seat> seats,
                  List<Crew> crew) {
        this.flightId = flightId;
        this.route = route;
        this.schedule = schedule;
        this.aircraft = aircraft;
        this.seats = seats;
        this.crew = crew;
        this.status = FlightStatus.SCHEDULED;
    }

}