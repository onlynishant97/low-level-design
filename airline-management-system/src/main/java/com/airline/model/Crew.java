package com.airline.model;

import com.airline.enums.CrewRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Crew {
    private final String crewId;
    private final CrewRole role;
}