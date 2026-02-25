package kr.tour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TourApplication {

  public static void main(String[] args) {
    SpringApplication.run(TourApplication.class, args);
  }

}
