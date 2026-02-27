package br.com.gabrielpaciullo.tests;

import br.com.gabrielpaciullo.client.AuthClient;
import br.com.gabrielpaciullo.client.ProductsClient;
import br.com.gabrielpaciullo.config.BaseTest;
import br.com.gabrielpaciullo.model.LoginRequest;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class AuthProductsTests extends BaseTest {

	private ProductsClient products;
	private AuthClient auth;

	@BeforeClass(alwaysRun = true)
	public void init() {
		products = new ProductsClient(spec);
		auth = new AuthClient(spec);
	}

	@Test
	public void deveAcessarAuthProductsComToken() {
		Response loginResponse = auth.login(new LoginRequest("emilys", "emilyspass")).then()
				.statusCode(anyOf(is(200), is(201))).extract().response();

		String token = loginResponse.path("accessToken");
		if (token == null || token.isBlank())
			token = loginResponse.path("token");

		// garante que conseguiu pegar
		org.testng.Assert.assertTrue(token != null && !token.isBlank(), "Token não retornado no login.");

		products.authProducts(token).then().statusCode(200).body("products", is(not(empty())));
	}

	@Test
	public void deveFalharAuthProductsSemToken() {
		// Sem token válido, normalmente retorna 401/403
		products.authProducts("token_invalido").then().statusCode(anyOf(is(401), is(403)));
	}
}