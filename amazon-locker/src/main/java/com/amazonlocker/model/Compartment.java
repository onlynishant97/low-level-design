package com.amazonlocker.model;

import com.amazonlocker.enums.Size;
import com.amazonlocker.enums.CompartmentStatus;
import lombok.Data;

@Data
public class Compartment {
    String compartmentId;
    Size size;
    CompartmentStatus status;
    Package storedPackage;

    public Compartment(String compartmentId, Size size) {
        this.compartmentId = compartmentId;
        this.size = size;
        this.status = CompartmentStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        return this.status == CompartmentStatus.AVAILABLE;
    }

    public void assignPackage(Package p) {
        if (status == CompartmentStatus.AVAILABLE) {
            this.storedPackage = p;
            this.status = CompartmentStatus.OCCUPIED;
        } else {
            throw new IllegalStateException("Compartment not available or package too large");
        }
    }

    public void releasePackage() {
        this.storedPackage = null;
        this.status = CompartmentStatus.AVAILABLE;
    }
}
