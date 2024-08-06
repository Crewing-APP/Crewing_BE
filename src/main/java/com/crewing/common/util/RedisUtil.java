package com.crewing.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;//Redis에 접근하기 위한 Spring의 Redis 템플릿 클래스
    private final ObjectMapper objectMapper;

//    public String getData(String key) {//지정된 키(key)에 해당하는 데이터를 Redis에서 가져오는 메서드
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        return valueOperations.get(key);
//    }

    //지정된 키(key)에 값을 저장하는 메서드
    public <T> boolean setData(String key, T data, Long duration) {
        try {
            String value = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(duration));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public <T> boolean setData(String key, T data) {
        try {
            String value = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public <T> T getData(String key, Class<T> classType) {
        try {
            return objectMapper.readValue(redisTemplate.opsForValue().get(key), classType);
        } catch (Exception e) {
            return null;
        }
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
