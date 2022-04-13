package com.hord.docusign;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class DSUserTokenHolder {

    private static final DSUserTokenHolder instance = new DSUserTokenHolder();
    private static final Map<String, DSToken> holder = new ConcurrentHashMap<>();

    private DSUserTokenHolder() {
    }

    public static DSUserTokenHolder getInstance() {
        return instance;
    }

    public Optional<DSToken> getToken(String userId) {
        return Optional.ofNullable(holder.get(userId));
    }

    public void set(String userId, DSToken token) {
        holder.put(userId, token);
    }
}
