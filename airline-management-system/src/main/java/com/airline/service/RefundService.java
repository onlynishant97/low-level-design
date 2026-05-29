package com.airline.service;

import com.airline.model.Booking;

public interface RefundService {

    void processRefund(Booking booking);
}