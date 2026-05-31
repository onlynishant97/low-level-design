package com.carrental.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {
    String id;
    String name;
    String dlNumber;
    String email;
    String phone;
}
