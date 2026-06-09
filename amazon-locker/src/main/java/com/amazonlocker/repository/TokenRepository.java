package com.amazonlocker.repository;

import com.amazonlocker.model.AccessToken;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TokenRepository {

    private final ConcurrentHashMap<String, AccessToken> store = new ConcurrentHashMap<>();

    public void save(AccessToken token) {
        store.put(token.getToken(), token);
    }

    public AccessToken find(String token) {
        return store.get(token);
    }

    public List<AccessToken> getAllUnusedToken() {
        return store.values().stream().filter(token -> !token.isUsed()).toList();
    }
}