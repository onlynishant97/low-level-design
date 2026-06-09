package com.amazonlocker.service;

public interface NotificationService {
    void sendPickupCode(String customerId, String token);
}
