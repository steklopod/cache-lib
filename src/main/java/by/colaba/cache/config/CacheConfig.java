package by.colaba.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass
@EnableCaching
@Configuration(proxyBeanMethods = false)
public class CacheConfig extends CachingConfigurerSupport {

    @Override
    public CacheErrorHandler errorHandler() {
        return new SilentCacheErrorHandler();
    }

    @Slf4j
    static class SilentCacheErrorHandler implements CacheErrorHandler {
        private enum Command {
            GET, PUT, EVICT, CLEAR
        }

        private void internalHandleException(@NotNull Command command, @NotNull RuntimeException exception, @NotNull Cache cache, Object key, Object value) {
            log.warn("Unable to {} action into cache={} (key = '{}', value = '{}'): {}",
                    command, cache.getName(), key, value, exception.getMessage());
        }

        @Override
        public void handleCacheGetError(@NotNull RuntimeException exception, @NotNull Cache cache, @NotNull Object key) {
            internalHandleException(Command.GET, exception, cache, key, null);
        }

        @Override
        public void handleCachePutError(@NotNull RuntimeException exception, @NotNull Cache cache, @NotNull Object key, Object value) {
            internalHandleException(Command.PUT, exception, cache, key, value);
        }

        @Override
        public void handleCacheEvictError(@NotNull RuntimeException exception, @NotNull Cache cache, @NotNull Object key) {
            internalHandleException(Command.EVICT, exception, cache, key, null);
        }

        @Override
        public void handleCacheClearError(@NotNull RuntimeException exception, @NotNull Cache cache) {
            internalHandleException(Command.CLEAR, exception, cache, null, null);
        }

    }

}
