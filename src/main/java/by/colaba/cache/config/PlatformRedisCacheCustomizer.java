package by.colaba.cache.config;

import org.springframework.data.redis.cache.RedisCacheConfiguration;

@FunctionalInterface
public interface PlatformRedisCacheCustomizer {
    void customize(String cache, RedisCacheConfiguration config);
}
