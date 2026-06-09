package com.amazonlocker;

import com.amazonlocker.enums.*;
import com.amazonlocker.model.*;
import com.amazonlocker.model.Package;
import com.amazonlocker.repository.PackageRepository;
import com.amazonlocker.repository.TokenRepository;
import com.amazonlocker.service.*;
import com.amazonlocker.strategy.BestFitStrategy;

import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        Compartment c1 = new Compartment("S1", Size.SMALL);
        Compartment c2 = new Compartment("M1", Size.MEDIUM);
        Compartment c3 = new Compartment("L1", Size.LARGE);

        Locker locker = new Locker("Locker1", Arrays.asList(c1, c2, c3));
        LockerLocation location = new LockerLocation("LOC-1", "Whitefield Station", List.of(locker));


        AllocationService allocationService = new AllocationService(new BestFitStrategy());
        PackageRepository packageRepository = new PackageRepository();
        TokenRepository tokenRepository = new TokenRepository();
        LockerService lockerService = new LockerService(allocationService, new TokenService(), new EmailNotificationService(), packageRepository, tokenRepository);
        ExpiryScheduler expiryScheduler = new ExpiryScheduler(packageRepository, tokenRepository);
        expiryScheduler.init();

        Package pkg = new Package("PKG1", "ORDER1", "cust1@email.com", Size.MEDIUM);
        AccessToken token = lockerService.depositPackage(pkg, locker);
        System.out.println("Token issued: " + token.getToken());
        System.out.println("Expires: " + token.getExpiryTime());

        PickupResult picked = lockerService.pickupPackage(token.getToken());

        System.out.println("Pickup success: " + picked);

        // --- Try picking up again (already used) ---
        PickupResult result2 = lockerService.pickupPackage(token.getToken());
        System.out.println("Second attempt: " + result2);  // ALREADY_USED

        expiryScheduler.destroy();
    }

}