package br.com.gabrielpaciullo.tests;

import br.com.gabrielpaciullo.assertions.ApiAssertions;
import br.com.gabrielpaciullo.client.AuthClient;
import br.com.gabrielpaciullo.client.ProductsClient;
import br.com.gabrielpaciullo.config.BaseTest;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AuthenticatedProductsTests extends BaseTest {

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

        ApiAssertions.shouldBeOk(response);
        ApiAssertions.shouldHaveNonEmptyArray(response, "products");
    }

    @Test
    public void shouldFailToAccessAuthProductsWithInvalidToken() {
        ValidatableResponse response = products.authProducts("token_invalido").then();

        ApiAssertions.shouldBeUnauthorized(response);
        ApiAssertions.shouldHaveUnauthorizedErrorDetails(response);
    }

    @Test
    public void shouldFailToAccessAuthProductsWithoutAuthorizationHeader() {
        ValidatableResponse response = products.authProductsWithoutToken().then();

        ApiAssertions.shouldBeUnauthorized(response);
        ApiAssertions.shouldHaveUnauthorizedErrorDetails(response);
    }

    @Test
    public void shouldFailLoginWithInvalidCredentials() {
        ValidatableResponse response = auth.login("usuario_inexistente", "senha_errada").then();

        ApiAssertions.shouldBeBadRequestUnauthorizedOrUnprocessableEntity(response);
        ApiAssertions.shouldHaveInvalidCredentialsErrorDetails(response);
    }

    private String getValidAccessToken() {
        return auth.loginWithDefaultCredentials()
                .then()
                .statusCode(Matchers.anyOf(Matchers.is(200), Matchers.is(201)))
                .body("accessToken", Matchers.not(Matchers.isEmptyOrNullString()))
                .extract()
                .path("accessToken");
    }
}
