package com.amazonlocker.strategy;

import com.amazonlocker.enums.Size;
import com.amazonlocker.model.Compartment;
import com.amazonlocker.model.Locker;

public class BestFitStrategy implements AllocationStrategy {

    @Override
    public Compartment allocate(Locker locker, Size size) {
        for (Compartment compartment : locker.getCompartments()) {
            if (compartment.isAvailable() && compartment.getSize().ordinal() >= size.ordinal()) {
                return compartment;
            }
        }
        return null;
    }
}
