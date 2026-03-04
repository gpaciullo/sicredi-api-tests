package br.com.gabrielpaciullo.client;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class HealthClient extends BaseClient {

    private static final String HEALTH = "/test";

    public HealthClient(RequestSpecification spec) {
        super(spec);
    }

    public Response health() {
        return get(HEALTH);
    }
}