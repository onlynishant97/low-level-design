package com.amazonlocker.model;

import lombok.Data;

import java.util.List;

@Data
public class Locker {
    String lockerId;
    List<Compartment> compartments;

    public Locker(String lockerId, List<Compartment> compartments) {
        this.lockerId = lockerId;
        this.compartments = compartments;
    }

    void addCompartment(Compartment compartment) {
        this.compartments.add(compartment);
    }

}
