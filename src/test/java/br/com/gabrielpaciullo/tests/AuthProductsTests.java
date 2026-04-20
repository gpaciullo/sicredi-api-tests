package br.com.gabrielpaciullo.tests;

import br.com.gabrielpaciullo.client.AuthClient;
import br.com.gabrielpaciullo.client.ProductsClient;
import br.com.gabrielpaciullo.config.BaseTest;
import br.com.gabrielpaciullo.model.LoginRequest;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
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
    public void shouldAccessAuthProductsWithValidToken() {
        String token = getValidAccessToken();

        ValidatableResponse response = products.authProducts(token).then();

        response.statusCode(200)
                .body("products", is(not(empty())));
    }

    @Test
    public void shouldFailToAccessAuthProductsWithInvalidToken() {
        ValidatableResponse response = products.authProducts("token_invalido").then();

        response.statusCode(anyOf(is(401), is(403)));
    }

    @Test
    public void shouldFailToAccessAuthProductsWithoutAuthorizationHeader() {
        ValidatableResponse response = given()
                .spec(spec)
                .when()
                .get("/auth/products")
                .then();

        response.statusCode(anyOf(is(401), is(403)));
    }

    @Test
    public void shouldFailLoginWithInvalidCredentials() {
        ValidatableResponse response = auth.login(new LoginRequest("usuario_inexistente", "senha_errada")).then();
        response.statusCode(anyOf(is(400), is(401), is(422)));
    }

    private String getValidAccessToken() {
        String username = System.getProperty("username", "emilys");
        String password = System.getProperty("password", "emilyspass");

        return auth.login(new LoginRequest(username, password))
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .body("accessToken", not(isEmptyOrNullString()))
                .extract()
                .path("accessToken");
    }
}