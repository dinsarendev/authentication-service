package com.cambofreelance.authenticationservice.caches;
import com.cambofreelance.authenticationservice.dto.ResponseCodeDto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ResponseCodeRedisCache {

  @Value("${storage.redis.key-response-code}")
  private String keyValue;

  private static String KEY;

  private static HashOperations<String, String, ResponseCodeDto> hashOperations;

  @Resource(name = "responseCodeRedisTemplate")
  private RedisTemplate<String, ResponseCodeDto> redisTemplate;

  @PostConstruct
  private void init() {
    KEY = keyValue;
    hashOperations = redisTemplate.opsForHash();
  }

  public static ResponseCodeDto getRespCode(final String code) {
    return hashOperations.get(KEY, code);
  }

  public static void initRespCodeCache(List<ResponseCodeDto> respCodes) {
    for (ResponseCodeDto responseCode : respCodes) {
      hashOperations.put(KEY, responseCode.getCode(), responseCode);
    }
  }

  public static void addRespCodeCache(final ResponseCodeDto respCode) {
    hashOperations.put(KEY, respCode.getCode(), respCode);
  }

  public static void reloadRespCode(final ResponseCodeDto respCode) {
    hashOperations.put(KEY, respCode.getCode(), respCode);
  }

  public static String getRespMessage(final String code) {
    ResponseCodeDto dto = hashOperations.get(KEY, code);
    return dto != null ? dto.getMessage() : null;
  }
}
