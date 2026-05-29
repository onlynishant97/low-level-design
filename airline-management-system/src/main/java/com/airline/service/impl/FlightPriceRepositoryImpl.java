package com.airline.service.impl;

import com.airline.enums.SeatClass;
import com.airline.model.FlightPrice;
import com.airline.repository.FlightPriceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FlightPriceRepositoryImpl implements FlightPriceRepository {
    ConcurrentHashMap<String, List<FlightPrice>> map = new ConcurrentHashMap<>();

    @Override
    public void save(FlightPrice flightPrice) {
        List<FlightPrice> flightPrices = map.getOrDefault(flightPrice.getFlightId(), new ArrayList<>());
        flightPrices.add(flightPrice);
        map.put(flightPrice.getFlightId(), flightPrices);
    }

    @Override
    public Optional<FlightPrice> findByFlightAndSeatClass(String flightId, SeatClass seatClass) {
        if (map.get(flightId) == null)
            return Optional.empty();
        return map.get(flightId).stream().filter(fp -> fp.getSeatClass() == seatClass).findFirst();
    }

    @Override
    public List<FlightPrice> findByFlight(String flightId) {
        return map.get(flightId);
    }
}
