package br.com.gabrielpaciullo.tests;

import br.com.gabrielpaciullo.assertions.ApiAssertions;
import br.com.gabrielpaciullo.client.ProductsClient;
import br.com.gabrielpaciullo.config.BaseTest;
import br.com.gabrielpaciullo.model.ProductRequest;
import br.com.gabrielpaciullo.utils.ProductFactory;
import br.com.gabrielpaciullo.utils.RandomUtils;
import br.com.gabrielpaciullo.utils.TestDataProvider;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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

        ValidatableResponse response = products.list(limit, skip).then();

        ApiAssertions.shouldBeOk(response);
        ApiAssertions.shouldHaveNonEmptyArray(response, "products");

        response.body("limit", equalTo(limit))
                .body("skip", equalTo(skip))
                .body("total", greaterThan(0));
    }

    @Test
    public void shouldGetProductById() {
        int id = 1;

        ValidatableResponse response = products.byId(id).then();

        ApiAssertions.shouldBeOk(response);
        response.body("id", equalTo(id));
        ApiAssertions.shouldHaveNonBlankField(response, "title");
    }

    @Test
    public void shouldReturnNotFoundWhenIdIsInvalid() {
        products.byId(999999).then()
                .statusCode(anyOf(is(404), is(400)));
    }

    @Test(dataProvider = "validProductData", dataProviderClass = TestDataProvider.class)
    public void shouldCreateProductSuccessfully(String baseTitle, double price) {
        String title = baseTitle + "-" + RandomUtils.randomProductName();
        ProductRequest request = ProductFactory.validProduct(title, price);

        ValidatableResponse response = products.add(request).then();

        ApiAssertions.shouldBeCreated(response);
        ApiAssertions.shouldHaveId(response);

        response.body("title", equalTo(title))
                .body("price", notNullValue());
    }

    @Test(dataProvider = "invalidProductData", dataProviderClass = TestDataProvider.class)
    public void shouldDocumentThatApiAcceptsInvalidDataWhenAddingProduct(String title, double price) {
        ValidatableResponse response = products.add(new ProductRequest(title, price)).then();

        ApiAssertions.shouldBeCreated(response);
        ApiAssertions.shouldHaveId(response);
        response.body("price", notNullValue());
    }

    @Test
    public void shouldDocumentPaginationBehaviorAtBoundaryValues() {
        ApiAssertions.shouldBeOk(products.list(0, 0).then());
        ApiAssertions.shouldBeBadRequestOrOk(products.list(10, -1).then());
    }

    @Test
    public void shouldDocumentBehaviorWhenPriceIsNegative() {
        ValidatableResponse response = products.add(ProductFactory.productWithNegativePrice()).then();

        ApiAssertions.shouldBeCreated(response);
        response.body("price", equalTo(-10));
    }

    @Test
    public void shouldDocumentBehaviorWhenTitleIsEmpty() {
        ValidatableResponse response = products.add(ProductFactory.productWithEmptyTitle()).then();

        ApiAssertions.shouldBeCreated(response);
        response.body("title", isEmptyString());
    }

    @Test
    public void shouldDocumentApiBehaviorForInvalidPagination() {
        ApiAssertions.shouldBeBadRequestOrOk(products.list(-1, -10).then());
    }

    @Test
    public void shouldEnsureMinimumContractWhenGettingProductById() {
        ValidatableResponse response = products.byId(1).then();

        ApiAssertions.shouldBeOk(response);
        ApiAssertions.shouldHaveProductCoreFields(response);
    }

    @Test
    public void shouldReturnUnauthorizedWhenAccessingProtectedEndpointWithoutToken() {
        ValidatableResponse response = products.authProductsWithoutToken().then();

        ApiAssertions.shouldBeUnauthorized(response);
    }

    @Test
    public void shouldDocumentProductListingStability() {
        int limit = 5;
        int skip = 0;

        for (int attempt = 0; attempt < 3; attempt++) {
            ValidatableResponse response = products.list(limit, skip).then();

            ApiAssertions.shouldBeOk(response);
            ApiAssertions.shouldHaveNonEmptyArray(response, "products");
        }
    }

    @Test
    public void shouldDocumentBehaviorWhenSkipExceedsTotal() {
        ValidatableResponse response = products.list(10, 999999).then();

        ApiAssertions.shouldBeOk(response);
        ApiAssertions.shouldHaveEmptyArray(response, "products");
    }
}
