package br.com.gabrielpaciullo.tests;

import br.com.gabrielpaciullo.config.BaseTest;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class SmokeTest extends BaseTest {

  @Test
  public void shouldHitHealth() {
    RestAssured.given()
      .when().get("/test")
      .then().statusCode(anyOf(is(200), is(204), is(404)));
  }
}