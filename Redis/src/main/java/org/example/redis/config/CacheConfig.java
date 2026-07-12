package org.example.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 1. 全局默认配置（兜底）
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))          // 默认 30 分钟
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(RedisSerializer.json())
                );

        // 2. 为特定缓存名定制配置
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        cacheConfigs.put("users", defaultConfig.entryTtl(Duration.ofHours(1)));      // users 缓存 1 小时
        cacheConfigs.put("products", defaultConfig.entryTtl(Duration.ofMinutes(10)));// products 缓存 10 分钟
        cacheConfigs.put("orders", defaultConfig.entryTtl(Duration.ofSeconds(30)));  // orders 缓存 30 秒

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)                 // 设置全局默认
                .withInitialCacheConfigurations(cacheConfigs) // 注入特定缓存名配置
                .build();
    }
}