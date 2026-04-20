package br.com.gabrielpaciullo.assertions;

import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static ValidatableResponse shouldHaveNonEmptyBody(ValidatableResponse response) {
        return response.body(not(isEmptyOrNullString()));
    }

    public static ValidatableResponse shouldHaveErrorMessageContainingAny(ValidatableResponse response, String... keywords) {
        List<Matcher<? super String>> matchers = Arrays.stream(keywords)
                .map(org.hamcrest.Matchers::containsStringIgnoringCase)
                .collect(Collectors.toList());

        return response.body(anyOf(matchers));
    }

    public static ValidatableResponse shouldHaveUnauthorizedErrorDetails(ValidatableResponse response) {
        shouldHaveNonEmptyBody(response);
        return shouldHaveErrorMessageContainingAny(response,
                "auth",
                "unauthorized",
                "forbidden",
                "token",
                "access"
        );
    }

    public static ValidatableResponse shouldHaveInvalidCredentialsErrorDetails(ValidatableResponse response) {
        shouldHaveNonEmptyBody(response);
        return shouldHaveErrorMessageContainingAny(response,
                "invalid",
                "credentials",
                "login",
                "user",
                "password",
                "auth"
        );
    }
}
