package kr.tour.global.fixture;


import kr.tour.global.DatabaseCleaner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import(value = {DatabaseCleaner.class})
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
public @interface ServiceTest {
}
