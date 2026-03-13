package kr.tour.global.config;

import kr.tour.global.exception.cache.CustomCacheErrorHandler;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CacheConfig implements CachingConfigurer {

  private final CacheManager cacheManager;
  private final CustomCacheErrorHandler customCacheErrorHandler;

  @Override
  public CacheManager cacheManager() {
    return cacheManager;
  }

  @Override
  public CacheErrorHandler errorHandler() {
    return customCacheErrorHandler;
  }
}