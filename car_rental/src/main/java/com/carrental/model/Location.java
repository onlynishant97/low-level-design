package com.carrental.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    String id;
    String name;
    String address;
    double latitude;
    double longitude;
}
