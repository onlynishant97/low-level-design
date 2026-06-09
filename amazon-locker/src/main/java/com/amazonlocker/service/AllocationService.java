package com.amazonlocker.service;

import com.amazonlocker.enums.Size;
import com.amazonlocker.model.Compartment;
import com.amazonlocker.model.Locker;
import com.amazonlocker.strategy.AllocationStrategy;

public class AllocationService {
    private final AllocationStrategy strategy;

    public AllocationService(AllocationStrategy strategy) {
        this.strategy = strategy;
    }

    public Compartment allocate(Locker locker, Size size) {
        return strategy.allocate(locker, size);
    }
}
