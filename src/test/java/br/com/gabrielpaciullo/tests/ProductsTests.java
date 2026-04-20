package br.com.gabrielpaciullo.tests;

import br.com.gabrielpaciullo.assertions.ApiAssertions;
import br.com.gabrielpaciullo.client.ProductsClient;
import br.com.gabrielpaciullo.config.BaseTest;
import br.com.gabrielpaciullo.model.ProductRequest;
import br.com.gabrielpaciullo.utils.ProductFactory;
import br.com.gabrielpaciullo.utils.RandomUtils;
import br.com.gabrielpaciullo.utils.TestDataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductsTests extends BaseTest {

    private ProductsClient products;

    @BeforeClass(alwaysRun = true)
    public void init() {
        products = new ProductsClient(spec);
    }

    @Test
    public void shouldListProductsWithPagination() {
        int limit = 10;
        int skip = 0;

        io.restassured.response.ValidatableResponse response = products.list(limit, skip).then();

        ApiAssertions.shouldBeOk(response);
        ApiAssertions.shouldHaveNonEmptyArray(response, "products");

        response.body("limit", equalTo(limit))
                .body("skip", equalTo(skip))
                .body("total", greaterThan(0));
    }

    @Test
    public void shouldGetProductById() {
        int id = 1;

        io.restassured.response.ValidatableResponse response = products.byId(id).then();

        ApiAssertions.shouldBeOk(response);

        response.body("id", equalTo(id))
                .body("title", not(isEmptyOrNullString()));
    }

    @Test
    public void shouldReturnNotFoundWhenIdIsInvalid() {
        products.byId(999999).then()
                // a API pode responder 404 ou 400 dependendo da implementação
                .statusCode(anyOf(is(404), is(400)));
    }

    @Test(dataProvider = "validProductData", dataProviderClass = TestDataProvider.class)
    public void shouldCreateProductSuccessfully(String baseTitle, double price) {
        String title = baseTitle + "-" + RandomUtils.randomProductName();

        io.restassured.response.ValidatableResponse response = products.add(ProductFactory.validProduct(title, price)).then();

        ApiAssertions.shouldBeCreated(response);
        ApiAssertions.shouldHaveId(response);

        response.body("title", equalTo(title))
                .body("price", notNullValue());
    }

    @Test(dataProvider = "invalidProductData", dataProviderClass = TestDataProvider.class)
    public void shouldDocumentThatApiAcceptsInvalidDataWhenAddingProduct(String title, double price) {
        // comportamento atual do DummyJSON: aceita dados inválidos e retorna 201/200
    	io.restassured.response.ValidatableResponse response = products.add(new ProductRequest(title, price)).then();

        ApiAssertions.shouldBeCreated(response);
        ApiAssertions.shouldHaveId(response);

        response.body("price", notNullValue());
    }

    @Test
    public void shouldDocumentPaginationBehaviorAtBoundaryValues() {
        ApiAssertions.shouldBeOk(products.list(0, 0).then());

        products.list(10, -1)
                .then()
                .statusCode(anyOf(is(200), is(400)));
    }

    @Test
    public void shouldDocumentBehaviorWhenPriceIsNegative() {
        ProductRequest request = new ProductRequest("Produto inválido QA", -10.0);

        io.restassured.response.ValidatableResponse response = products.add(request).then();

        ApiAssertions.shouldBeCreated(response);

        response.body("price", equalTo(-10));
    }

    @Test
    public void shouldDocumentBehaviorWhenTitleIsEmpty() {
        ProductRequest request = new ProductRequest("", 100.0);

        io.restassured.response.ValidatableResponse response = products.add(request).then();

        ApiAssertions.shouldBeCreated(response);

        response.body("title", isEmptyString());
    }

    @Test
    public void shouldDocumentApiBehaviorForInvalidPagination() {
        products.list(-1, -10)
                .then()
                .statusCode(anyOf(is(200), is(400)));
    }

    @Test
    public void shouldEnsureMinimumContractWhenGettingProductById() {
        int id = 1;

        io.restassured.response.ValidatableResponse response = products.byId(id).then();

        ApiAssertions.shouldBeOk(response);

        response.body("id", notNullValue())
                .body("title", notNullValue())
                .body("price", notNullValue())
                .body("category", notNullValue());
    }

    @Test
    public void shouldReturnUnauthorizedWhenAccessingProtectedEndpointWithoutToken() {
    	io.restassured.response.ValidatableResponse response = given()
                .spec(spec)
                .when()
                .get("/auth/products")
                .then();

        ApiAssertions.shouldBeUnauthorized(response);
    }

    @Test
    public void shouldDocumentProductListingStability() {
        int limit = 5;
        int skip = 0;

        for (int i = 0; i < 3; i++) {
        	io.restassured.response.ValidatableResponse response = products.list(limit, skip).then();

            ApiAssertions.shouldBeOk(response);
            ApiAssertions.shouldHaveNonEmptyArray(response, "products");
        }
    }

    @Test
    public void shouldDocumentBehaviorWhenSkipExceedsTotal() {
        products.list(10, 999999)
                .then()
                .statusCode(200)
                .body("products", is(empty()));
    }
}