package com.amazonlocker.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
public class AccessToken {
    String token;
    LocalDateTime expiryTime;
    private final AtomicBoolean isUsed = new AtomicBoolean(false); // FIX: was plain boolean, not thread-safe
    String packageId;

    public AccessToken(String token, LocalDateTime expiryTime, String packageId) {
        this.token = token;
        this.expiryTime = expiryTime;
        this.packageId = packageId;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }

    public boolean tryMarkAsUsed() {
        return isUsed.compareAndSet(false, true);
    }

    public boolean isUsed() {
        return isUsed.get();
    }


}