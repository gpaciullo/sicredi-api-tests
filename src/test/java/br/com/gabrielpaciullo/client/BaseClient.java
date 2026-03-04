package br.com.gabrielpaciullo.client;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class BaseClient {

    protected final RequestSpecification spec;

    protected BaseClient(RequestSpecification spec) {
        this.spec = spec;
    }

    protected RequestSpecification request() {
        return given()
                .spec(spec)
                .log().ifValidationFails();
    }

    protected Response get(String path) {
        return request()
                .when()
                .get(path);
    }

    protected Response get(String path, Map<String, ?> queryParams) {
        return request()
                .queryParams(queryParams)
                .when()
                .get(path);
    }

    protected Response getWithBearer(String path, String token) {
        return request()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(path);
    }

    protected Response post(String path, Object body) {
        return request()
                .body(body)
                .when()
                .post(path);
    }
}