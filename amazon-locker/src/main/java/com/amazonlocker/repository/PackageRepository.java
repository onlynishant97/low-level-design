package com.amazonlocker.repository;

import com.amazonlocker.model.Package;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PackageRepository {
    private final ConcurrentHashMap<String, Package> store = new ConcurrentHashMap<>();

    public void save(Package pkg) {
        store.put(pkg.getPackageId(), pkg);
    }

    public Package findById(String packageId) {
        return store.get(packageId);
    }

    public boolean exists(String packageId) {
        return store.containsKey(packageId);
    }

    public List<Package> findAll() {
        return new ArrayList<>(store.values());
    }
}
