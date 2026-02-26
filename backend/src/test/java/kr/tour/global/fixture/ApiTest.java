package kr.tour.global.fixture;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

public abstract class ApiTest {

  @LocalServerPort
  protected int port;

  protected RequestSpecification spec;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
    this.spec = new RequestSpecBuilder()
            .addHeader("Device-Uuid", UUID.randomUUID().toString())
            .build();
  }
}
