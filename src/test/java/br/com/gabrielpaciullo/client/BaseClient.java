package br.com.gabrielpaciullo.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public abstract class BaseClient {

	protected final RequestSpecification spec;

	protected BaseClient(RequestSpecification spec) {
		this.spec = spec;
	}

	protected Response get(String path) {
		return RestAssured.given().spec(spec).when().get(path);
	}

	protected Response get(String path, Map<String, ?> queryParams) {
		return RestAssured.given().spec(spec).queryParams(queryParams).when().get(path);
	}

	protected Response getWithBearer(String path, String token) {
		return RestAssured.given().spec(spec).header("Authorization", "Bearer " + token).when().get(path);
	}

	protected Response post(String path, Object body) {
		return RestAssured.given().spec(spec).body(body).when().post(path);
	}
}