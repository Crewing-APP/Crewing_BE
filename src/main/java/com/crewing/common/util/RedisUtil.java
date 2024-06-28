package com.crewing.common.util;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;//Redis에 접근하기 위한 Spring의 Redis 템플릿 클래스

    public String getData(String key) {//지정된 키(key)에 해당하는 데이터를 Redis에서 가져오는 메서드
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value) {//지정된 키(key)에 값을 저장하는 메서드
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        redisTemplate.expire(key, 8, TimeUnit.HOURS);
        valueOperations.set(key, value);
    }

    public Object getHashData(String key, Object hashKey) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, hashKey);
    }

    public void setHashData(String key, String hashKey, String value) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        redisTemplate.expire(key, 6, TimeUnit.HOURS);
        hashOperations.put(key, hashKey, value);

    }

    public void setDataExpire(String key, String value,
                              long duration) {//지정된 키(key)에 값을 저장하고, 지정된 시간(duration) 후에 데이터가 만료되도록 설정하는 메서드
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {//지정된 키(key)에 해당하는 데이터를 Redis에서 삭제하는 메서드
        redisTemplate.delete(key);
    }

}
