package com.jinwoo.dev.sns.repository;

import com.jinwoo.dev.sns.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, User> userRedisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(User user){
        String key = getKey(user.getUsername());
        log.info("set user to redis  {}({})", key, user);
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL); //캐싱을한 사용자의 데이터가 사용을 안하는데도 무한히 저장이 되어있다면 낭비가 되기 때문에 만료시간을 설정함.
    }

    public Optional<User> getUser(String userName){
        String key = getKey(userName);
        User user = userRedisTemplate.opsForValue().get(key);
        log.info("get data from redis {}({})", key, user);

        return Optional.ofNullable(user);
    }

    private String getKey(String userName){
        return "USER:" + userName;
    }
}
