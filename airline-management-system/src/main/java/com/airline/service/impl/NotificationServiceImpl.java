package com.airline.service.impl;

import com.airline.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {

    @Override
    public void send(String email, String message) {
        System.out.println(
                "Notification sent to " + email + ": " + message
        );
    }
}