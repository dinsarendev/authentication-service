package com.cambofreelance.authenticationservice.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisRepositories
@EnableRedisHttpSession
public class RedisConfiguration {

    @Value("${spring.data.redis.host: ''}")
    private String redisHost;

    @Value("${spring.data.redis.port: ''}")
    private int redisPort;

    @Value("${spring.data.redis.sentinel.master: ''}")
    private String sentinelMaster;

    @Value("${spring.data.redis.sentinel.nodes: ''}")
    private String sentinelNodes;

    @Value("${spring.data.redis.password: ''}")
    private String redisPassword;

    @Value("${spring.data.redis.enabledSSL: true}")
    private boolean enabledSSL;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        if (!sentinelMaster.equals(" ''")) {
            RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
            sentinelConfig.master(sentinelMaster);
            for (String node : sentinelNodes.split(",")) {
                String[] split = node.split(":");
                sentinelConfig.sentinel(split[0], Integer.parseInt(split[1]));
            }
            return new JedisConnectionFactory(sentinelConfig);
        } else {
            RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
            configuration.setHostName(redisHost);
            configuration.setPort(redisPort);
            if (!redisPassword.equals(" ''")) {
                configuration.setPassword(redisPassword);
            }

            if (enabledSSL) {
                JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().useSsl().and()
                        .usePooling().build();
                return new JedisConnectionFactory(configuration, jedisClientConfiguration);
            }

            return new JedisConnectionFactory(configuration);
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Set key serializer
        RedisSerializer<String> keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

        // Set value serializer
        RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration).build();
    }

}