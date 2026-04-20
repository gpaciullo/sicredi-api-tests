package br.com.gabrielpaciullo.assertions;

import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.*;

public final class ApiAssertions {

    private ApiAssertions() {
    }

    public static ValidatableResponse shouldBeCreated(ValidatableResponse response) {
        return response.statusCode(anyOf(is(200), is(201)));
    }

    public static ValidatableResponse shouldBeOk(ValidatableResponse response) {
        return response.statusCode(200);
    }

    public static ValidatableResponse shouldBeUnauthorized(ValidatableResponse response) {
        return response.statusCode(anyOf(is(401), is(403)));
    }

    public static ValidatableResponse shouldBeBadRequestOrOk(ValidatableResponse response) {
        return response.statusCode(anyOf(is(200), is(400)));
    }

    public static ValidatableResponse shouldBeBadRequestUnauthorizedOrUnprocessableEntity(ValidatableResponse response) {
        return response.statusCode(anyOf(is(400), is(401), is(422)));
    }

    public static ValidatableResponse shouldHaveNonEmptyArray(ValidatableResponse response, String path) {
        return response.body(path, is(not(empty())));
    }

    public static ValidatableResponse shouldHaveEmptyArray(ValidatableResponse response, String path) {
        return response.body(path, is(empty()));
    }

    public static ValidatableResponse shouldHaveId(ValidatableResponse response) {
        return response.body("id", notNullValue());
    }

    public static ValidatableResponse shouldHaveNonBlankField(ValidatableResponse response, String path) {
        return response.body(path, not(isEmptyOrNullString()));
    }

    public static ValidatableResponse shouldHaveProductCoreFields(ValidatableResponse response) {
        return response
                .body("id", notNullValue())
                .body("title", notNullValue())
                .body("price", notNullValue())
                .body("category", notNullValue());
    }
}
