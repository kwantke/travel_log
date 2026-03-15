package kr.tour.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.lettuce.core.ClientOptions;
import kr.tour.global.util.PageDeserializer;
import kr.tour.global.util.SortDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {
  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;
  @Value("${spring.data.redis.ttl}")
  private int cacheTtlMinutes;

  @Value("${spring.data.redis.connect-timeout:5000}")
  private int connectTimeout;

  private Duration getCacheTtl() {
    return Duration.ofMinutes(cacheTtlMinutes);
  }


  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration redisConfig =
            new RedisStandaloneConfiguration(host, port);

    ClientOptions clientOptions = ClientOptions.builder()
            .autoReconnect(true)   // 재접속 루프 최소화, 실패 빠르게
            .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS) // 연결 끊겼을 때 에러 빠르게 발생
            .build();

    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofMillis(connectTimeout))
            .shutdownTimeout(Duration.ofMillis(100))
            .clientOptions(clientOptions)
            .build();

    return new LettuceConnectionFactory(redisConfig, clientConfig);
  }


  @Bean
  public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(getRedisCacheConfiguration())
            .build();
  }

  private RedisCacheConfiguration getRedisCacheConfiguration() {
    RedisSerializationContext.SerializationPair<String> keySerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
    RedisSerializationContext.SerializationPair<Object> valueSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(
            new GenericJackson2JsonRedisSerializer(getObjectMapperForRedisCacheManager())
    );

    return RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(keySerializationPair)
            .serializeValuesWith(valueSerializationPair)
            .entryTtl(getCacheTtl())
            .disableCachingNullValues();
  }

  private ObjectMapper getObjectMapperForRedisCacheManager() {
    ObjectMapper objectMapper = new ObjectMapper();

    SimpleModule pageModule = new SimpleModule();
    pageModule.addDeserializer(PageImpl.class, new PageDeserializer());
    pageModule.addDeserializer(Sort.class, new SortDeserializer());

    objectMapper.registerModules(new JavaTimeModule(), pageModule);
    objectMapper.activateDefaultTyping(
            BasicPolymorphicTypeValidator.builder().allowIfBaseType(Object.class).build(),
            ObjectMapper.DefaultTyping.EVERYTHING
    );

    return objectMapper;
  }
}