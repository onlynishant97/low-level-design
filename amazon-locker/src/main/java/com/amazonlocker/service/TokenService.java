package com.amazonlocker.service;

import com.amazonlocker.model.AccessToken;
import com.amazonlocker.model.Package;

import java.time.LocalDateTime;
import java.util.UUID;

public class TokenService {
    private final int expiryDays = 3;

    public TokenService() {
    }

    public AccessToken generateToken(Package pkg) {
        return new AccessToken(UUID.randomUUID().toString(), LocalDateTime.now().plusDays(expiryDays), pkg.getPackageId());
    }
}
