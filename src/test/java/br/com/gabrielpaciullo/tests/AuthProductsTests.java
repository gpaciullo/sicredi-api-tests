package br.com.gabrielpaciullo.tests;

import br.com.gabrielpaciullo.client.AuthClient;
import br.com.gabrielpaciullo.client.ProductsClient;
import br.com.gabrielpaciullo.config.BaseTest;
import br.com.gabrielpaciullo.model.LoginRequest;
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
		String username = System.getProperty("username", "emilys");
		String password = System.getProperty("password", "emilyspass");

		String token = auth.login(new LoginRequest(username, password)).then().statusCode(anyOf(is(200), is(201)))
				.body("accessToken", not(isEmptyOrNullString())).extract().path("accessToken");

		products.authProducts(token).then().statusCode(200).body("products", is(not(empty())));
	}

	@Test
	public void deveFalharAuthProductsSemToken() {
		// Sem token válido, normalmente retorna 401/403
		products.authProducts("token_invalido").then().statusCode(anyOf(is(401), is(403)));
	}
}