package br.com.gabrielpaciullo.tests;

import br.com.gabrielpaciullo.client.HealthClient;
import br.com.gabrielpaciullo.config.BaseTest;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.notNullValue;

public class SmokeTest extends BaseTest {

	@Test(groups = "smoke")
    public void shouldCallHealthCheckEndpoint() {

        HealthClient healthClient = new HealthClient(spec);

        healthClient.health()
                .then()
                .statusCode(200)
                .body(notNullValue());
    }
}