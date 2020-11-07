package com.alan.databee.common.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheStats;
import com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

public class ClassCache implements Cache<String, Class<?>> {

    @Override
    public @Nullable Class<?> getIfPresent(Object o) {
        return null;
    }

    @Override
    public Class<?> get(String s, Callable<? extends Class<?>> callable) throws ExecutionException {
        return null;
    }

    @Override
    public ImmutableMap<String, Class<?>> getAllPresent(Iterable<?> iterable) {
        return null;
    }

    @Override
    public void put(String s, Class<?> aClass) {

    }

    @Override
    public void putAll(Map<? extends String, ? extends Class<?>> map) {

    }

    @Override
    public void invalidate(Object o) {

    }

    @Override
    public void invalidateAll(Iterable<?> iterable) {

    }

    @Override
    public void invalidateAll() {

    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public CacheStats stats() {
        return null;
    }

    @Override
    public ConcurrentMap<String, Class<?>> asMap() {
        return null;
    }

    @Override
    public void cleanUp() {

    }
}
