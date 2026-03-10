package kr.tour.global.fixture;

import com.redis.testcontainers.RedisContainer;
import kr.tour.TourApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@SpringBootTest(
        classes = TourApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class IntegrationTest extends ApiTest {


  private static final RedisContainer redisContainer;

  static {
    redisContainer = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG));

    redisContainer.start();
  }

  @DynamicPropertySource
  private static void dynamicProperties(DynamicPropertyRegistry registry) {
    // add Redis Properties
    registry.add("spring.data.redis.host", redisContainer::getHost);
    registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
  }
}
