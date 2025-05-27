package com.cambofreelance.authenticationservice.caches;

import com.cambofreelance.authenticationservice.models.ResponseCode;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ResponseCodeRedisCache {

  private static final String KEY = "app:responseCodes";
  private static HashOperations<String, String, ResponseCode> hashOperations;

  @Resource
  private RedisTemplate<String, Object> redisTemplate;

  @PostConstruct
  private void init() {
    hashOperations = redisTemplate.opsForHash();
  }

  public void initRespCodeCache(List<ResponseCode> respCodes) {
    for (ResponseCode responseCode : respCodes) {
      hashOperations.put(KEY, responseCode.getCode(), responseCode);
    }
  }

  public static void addRespCodeCache(final ResponseCode respCode) {
    hashOperations.put(KEY, respCode.getCode(), respCode);
  }

  public static void reloadRespCode(final ResponseCode respCode) {
    hashOperations.put(KEY, respCode.getCode(), respCode);
  }

  public static ResponseCode getRespCode(final String code) {
    return hashOperations.get(KEY, code);
  }

  public static String getRespMessage(final String message) {
    return hashOperations.get(KEY, message).getMessage();
  }
}
