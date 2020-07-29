package by.colaba.cache.config;

import by.colaba.cache.PlatformCache;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.time.Duration;

import static java.util.Optional.ofNullable;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class RedisConfigCustomizer implements RedisCacheManagerBuilderCustomizer {

    private final ResourceLoader resourceLoader;
    private final CacheProperties cacheProperties;
    private final ObjectProvider<PlatformRedisCacheCustomizer> cacheCustomizers;

    @Override
    public void customize(RedisCacheManager.RedisCacheManagerBuilder builder) {
        builder.cacheDefaults(cacheDefaults(cacheProperties, resourceLoader.getClassLoader()))
                .initialCacheNames(PlatformCache.all());
        cacheCustomizers.orderedStream().forEach(it -> customizeCache(it, builder));
    }

    private void customizeCache(PlatformRedisCacheCustomizer customizer,
                                RedisCacheManager.RedisCacheManagerBuilder builder) {
        builder.getConfiguredCaches()
                .forEach(cache -> builder.getCacheConfigurationFor(cache)
                        .ifPresent(it -> customizer.customize(cache, it)));
    }

    private RedisCacheConfiguration cacheDefaults(CacheProperties cacheProperties, ClassLoader classLoader) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(fromSerializer(new JdkSerializationRedisSerializer(classLoader)))
                .entryTtl(ofNullable(redisProperties.getTimeToLive()).orElseGet(() -> Duration.ofMinutes(30)))
                .disableCachingNullValues();
        configuration.usePrefix();
        return configuration;
    }

    /*
    @Bean
    fun redisCacheManager(factory:RedisConnectionFactory): RedisCacheManager = builder(factory).cacheDefaults(
            RedisCacheConfiguration.defaultCacheConfig()
            .disableCachingNullValues()
            .serializeValuesWith(fromSerializer(GenericJackson2JsonRedisSerializer()))
            ).build()

    @Bean
    fun cacheProperties() = CacheProperties()
    */
}
