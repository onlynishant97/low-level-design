package com.amazonlocker.service;

public class EmailNotificationService implements NotificationService {
    @Override
    public void sendPickupCode(String customerId, String token) {
        // Simulate sending an email with the pickup code
        System.out.println("Sending email to customer " + customerId + " with pickup code: " + token);
    }
}
