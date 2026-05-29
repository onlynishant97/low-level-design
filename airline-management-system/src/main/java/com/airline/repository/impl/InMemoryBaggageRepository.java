package com.airline.repository.impl;

import com.airline.model.Baggage;
import com.airline.repository.BaggageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBaggageRepository implements BaggageRepository {

    // FlightID to Baggage Details
    ConcurrentHashMap<String, List<Baggage>> map = new ConcurrentHashMap<>();

    @Override
    public void save(Baggage baggage) {
        List<Baggage> baggages = map.getOrDefault(baggage.getFlightId(), new ArrayList<>());
        baggages.add(baggage);
        map.put(baggage.getFlightId(), baggages);
    }

    @Override
    public List<Baggage> getByFlightId(String flightId) {
        return List.copyOf(map.getOrDefault(flightId, new ArrayList<>()));
    }

    @Override
    public Baggage getByFlightIdAndPassengerId(String flightId, String passengerId) {
        return map.getOrDefault(flightId, new ArrayList<>())
                .stream()
                .filter(b -> b.getPassengerId().equals(passengerId))
                .findFirst()
                .orElse(null);
    }
}
