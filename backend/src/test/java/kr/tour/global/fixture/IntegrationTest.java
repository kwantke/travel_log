package kr.tour.global.fixture;

import kr.tour.TourApplication;
import kr.tour.global.config.TestJpaAuditingConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import({TestJpaAuditingConfig.class})
@ActiveProfiles("test")
@SpringBootTest(
        classes = TourApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class IntegrationTest extends ApiTest {

}
