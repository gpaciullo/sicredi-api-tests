package br.com.gabrielpaciullo.assertions;

import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.*;

public class ApiAssertions {

	private ApiAssertions() {
	}

	public static ValidatableResponse shouldBeCreated(ValidatableResponse res) {
		return res.statusCode(anyOf(is(200), is(201)));
	}

	public static ValidatableResponse shouldBeOk(ValidatableResponse res) {
		return res.statusCode(200);
	}

	public static ValidatableResponse shouldBeUnauthorized(ValidatableResponse res) {
		return res.statusCode(anyOf(is(401), is(403)));
	}

	public static ValidatableResponse shouldHaveNonEmptyArray(ValidatableResponse res, String path) {
		return res.body(path, is(not(empty())));
	}

	public static ValidatableResponse shouldHaveId(ValidatableResponse res) {
		return res.body("id", notNullValue());
	}
}