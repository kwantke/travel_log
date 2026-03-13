package kr.tour.global.exception.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomCacheErrorHandler implements CacheErrorHandler {

  @Override
  public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
    log.warn("Cache GET error. cache={}, key={}, message={}",
            cache != null ? cache.getName() : "unknown",
            key,
            exception.getMessage());
    // 예외를 던지지 않음 -> DB 조회로 진행
  }

  @Override
  public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
    log.warn("Cache PUT error. cache={}, key={}, message={}",
            cache != null ? cache.getName() : "unknown",
            key,
            exception.getMessage());
    // 예외를 던지지 않음 -> 응답은 정상 반환
  }

  @Override
  public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
    log.warn("Cache EVICT error. cache={}, key={}, message={}",
            cache != null ? cache.getName() : "unknown",
            key,
            exception.getMessage());
  }

  @Override
  public void handleCacheClearError(RuntimeException exception, Cache cache) {
    log.warn("Cache CLEAR error. cache={}, message={}",
            cache != null ? cache.getName() : "unknown",
            exception.getMessage());
  }
}
