package com.amazonlocker.service;

import com.amazonlocker.enums.PackageStatus;
import com.amazonlocker.model.AccessToken;
import com.amazonlocker.model.Package;
import com.amazonlocker.repository.PackageRepository;
import com.amazonlocker.repository.TokenRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class ExpiryScheduler {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final PackageRepository packageRepository;
    private final TokenRepository tokenRepository;

    public ExpiryScheduler(PackageRepository packageRepository, TokenRepository tokenRepository) {
        this.packageRepository = packageRepository;
        this.tokenRepository = tokenRepository;
    }

    public void init() {
        executor.scheduleAtFixedRate(this::expirePackages, 0, 1, TimeUnit.MINUTES);
    }

    public void expirePackages() {
        Map<String, AccessToken> tokensMap = tokenRepository.getAllUnusedToken().stream().collect(Collectors.toMap(token -> token.getPackageId(), at -> at));
        packageRepository.findAll().stream().filter(pkg -> {
            AccessToken token = tokensMap.get(pkg.getPackageId());
            return token.isExpired();
        }).forEach(pkg -> {
            pkg.setStatus(PackageStatus.EXPIRED);
            pkg.getAssignedCompartment().releasePackage();
            packageRepository.save(pkg);
        });
    }

    public void destroy() {
        executor.shutdown();
    }
}