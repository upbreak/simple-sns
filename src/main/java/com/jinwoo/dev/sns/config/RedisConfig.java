package com.jinwoo.dev.sns.config;

import com.jinwoo.dev.sns.model.User;
import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    public RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, User> userRedisTemplate = new RedisTemplate<>();
        userRedisTemplate.setConnectionFactory(redisConnectionFactory);
        userRedisTemplate.setKeySerializer(new StringRedisSerializer());
        userRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<User>(User.class));

        return userRedisTemplate;
    }
}
