package com.airline.repository;

import com.airline.model.Baggage;

import java.util.List;

public interface BaggageRepository {
    void save(Baggage baggage);

    List<Baggage> getByFlightId(String flightId);

    Baggage getByFlightIdAndPassengerId(String flightId, String passengerId);
}
