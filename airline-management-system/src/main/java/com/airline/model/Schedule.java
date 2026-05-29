package com.airline.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Schedule {

    private final LocalDateTime departure;
    private final LocalDateTime arrival;


}