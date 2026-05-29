package com.airline.model;

import com.airline.enums.UserRole;
import lombok.Data;

@Data
public class AdminUser extends User {
    public AdminUser(String username) {
        super(username, UserRole.ADMIN);
    }
}
