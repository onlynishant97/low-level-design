package com.amazonlocker.service;

import com.amazonlocker.enums.PackageStatus;
import com.amazonlocker.enums.PickupResult;
import com.amazonlocker.model.*;
import com.amazonlocker.model.Package;
import com.amazonlocker.repository.PackageRepository;
import com.amazonlocker.repository.TokenRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LockerService {

    private final AllocationService allocationService;
    private final TokenService tokenService;
    private final NotificationService notificationService;
    private final PackageRepository packageRepository;
    private final TokenRepository tokenRepository;

    private final ConcurrentHashMap<String, ReentrantLock> lockerLocks = new ConcurrentHashMap<>();

    public LockerService(AllocationService allocationService, TokenService tokenService, NotificationService notificationService, PackageRepository packageRepository, TokenRepository tokenRepository) {
        this.allocationService = allocationService;
        this.tokenService = tokenService;
        this.notificationService = notificationService;
        this.packageRepository = packageRepository;
        this.tokenRepository = tokenRepository;
    }

    public AccessToken depositPackage(Package pkg, LockerLocation location) {
        for (Locker locker : location.getLockers()) {
            Compartment candidate = allocationService.allocate(locker, pkg.getSize());
            if (candidate != null) {
                return depositPackage(pkg, locker);
            }
        }
        throw new RuntimeException("No available compartment at location: " + location.getName());
    }

    public AccessToken depositPackage(Package pkg, Locker locker) {

        if (packageRepository.exists(pkg.getPackageId())) {
            throw new IllegalStateException("Package " + pkg.getPackageId() + " is already deposited.");
        }

        Compartment compartment = allocationService.allocate(locker, pkg.getSize());

        if (compartment == null) {
            throw new RuntimeException("No compartment available");
        }

        ReentrantLock lock = lockerLocks.computeIfAbsent(compartment.getCompartmentId(), k -> new ReentrantLock());
        try {
            lock.lock();
            if (compartment.isAvailable()) {
                compartment.assignPackage(pkg);
                pkg.setAssignedCompartment(compartment);
                packageRepository.save(pkg);

                AccessToken token = tokenService.generateToken(pkg);
                tokenRepository.save(token);

                notificationService.sendPickupCode(pkg.getPackageId(), token.getToken());
                return token;

            } else {
                throw new RuntimeException("Compartment was taken by another thread");
            }

        } finally {
            lock.unlock();
        }
    }

    public PickupResult pickupPackage(String tokenCode) {
        AccessToken token = tokenRepository.find(tokenCode);
        if (token == null) {
            return PickupResult.INVALID_TOKEN;
        }
        if (token.isExpired()) {
            return PickupResult.EXPIRED;
        }

        if (token.isUsed()) {
            return PickupResult.ALREADY_USED;
        }

        if (!token.tryMarkAsUsed()) {
            return PickupResult.ALREADY_USED;
        }

        Package pkg = packageRepository.findById(token.getPackageId());
        if (pkg != null) {
            Compartment compartment = pkg.getAssignedCompartment();
            pkg.setStatus(PackageStatus.PICKED_UP);
            if (compartment != null) {
                compartment.releasePackage();
            }
            System.out.printf("[PICKUP] Package %s collected successfully.%n", pkg.getPackageId());
        }
        return PickupResult.SUCCESS;
    }
}


