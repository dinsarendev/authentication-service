package com.cambofreelance.authenticationservice.caches;

import com.cambofreelance.authenticationservice.dto.ResponseCodeDto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ResponseCodeRedisCache {

  private static final String KEY = "sys_response_code";

  private static HashOperations<String, String, ResponseCodeDto> hashOperations;

  @Resource(name = "responseCodeRedisTemplate")
  private RedisTemplate<String, ResponseCodeDto> redisTemplate;

  @PostConstruct
  private void init() {
    hashOperations = redisTemplate.opsForHash();
  }

  public static ResponseCodeDto getRespCode(final String code) {
    return hashOperations.get(KEY, code);
  }

}
