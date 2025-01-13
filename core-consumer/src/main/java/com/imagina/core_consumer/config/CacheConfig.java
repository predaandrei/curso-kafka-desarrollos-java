package com.imagina.core_consumer.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class CacheConfig {

    @Bean(name = "cacheSolicitudCompra")
    public Cache<UUID, Boolean> cacheSolicitudCompra() {
        return Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(2)).maximumSize(1000).build();
    }

}
