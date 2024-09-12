package com.fred.common.security;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 
 * @Description: LocalCache
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
public class LocalCache {

    private final Cache<String, Object> cache;

    public LocalCache() {
        cache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).maximumSize(100000).build();
    }

    public void putObject(String key, Object object) {
        cache.put(key, object);
    }

    public Object getObject(String key) {
        return cache.getIfPresent(key);
    }

    public void removeObject(String key) {
        cache.invalidate(key);
    }

    public long getSize() {
        return cache.size();
    }

    public boolean hasKey(String key) {
        return cache.getIfPresent(key) != null;
    }

    public void clear() {
        cache.cleanUp();
    }

}
