package com.airline.model;

import com.airline.enums.UserRole;
import lombok.Data;

@Data
public class AirlineStaff extends User {
    String department;

    AirlineStaff(String department, String username) {
        super(username, UserRole.STAFF);
        this.department = department;
    }
}
