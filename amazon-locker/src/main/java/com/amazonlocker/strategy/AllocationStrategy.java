package com.amazonlocker.strategy;

import com.amazonlocker.enums.Size;
import com.amazonlocker.model.Compartment;
import com.amazonlocker.model.Locker;

public interface AllocationStrategy {
    Compartment allocate(Locker locker, Size size);
}
