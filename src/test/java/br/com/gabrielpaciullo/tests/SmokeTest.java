package br.com.gabrielpaciullo.tests;

import br.com.gabrielpaciullo.assertions.ApiAssertions;
import br.com.gabrielpaciullo.client.HealthClient;
import br.com.gabrielpaciullo.config.BaseTest;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.notNullValue;

public class SmokeTest extends BaseTest {

    private HealthClient healthClient;

    @BeforeClass(alwaysRun = true)
    public void init() {
        healthClient = new HealthClient(spec);
    }

    @Test(groups = "smoke")
    public void shouldCallHealthCheckEndpoint() {
        ValidatableResponse response = healthClient.health().then();

        ApiAssertions.shouldBeOk(response);
        response.body(notNullValue());
    }
}
