package br.com.gabrielpaciullo.tests;

import br.com.gabrielpaciullo.assertions.ApiAssertions;
import br.com.gabrielpaciullo.client.ProductsClient;
import br.com.gabrielpaciullo.config.BaseTest;
import br.com.gabrielpaciullo.model.ProductRequest;
import br.com.gabrielpaciullo.utils.ProductFactory;
import br.com.gabrielpaciullo.utils.TestDataProvider;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class ProductsTests extends BaseTest {

    private static final int DEFAULT_LIMIT = 10;
    private static final int DEFAULT_SKIP = 0;
    private static final int KNOWN_PRODUCT_ID = 1;

    private ProductsClient products;

    @BeforeClass(alwaysRun = true)
    public void init() {
        products = new ProductsClient(spec);
    }

    @Test
    public void shouldListProductsWithPagination() {
        ValidatableResponse response = products.list(DEFAULT_LIMIT, DEFAULT_SKIP).then();

        ApiAssertions.shouldBeOk(response);
        ApiAssertions.shouldHaveNonEmptyArray(response, "products");
        ApiAssertions.shouldHavePaginationMetadata(response, DEFAULT_LIMIT, DEFAULT_SKIP);
    }

    @Test
    public void shouldGetProductById() {
        ValidatableResponse response = products.byId(KNOWN_PRODUCT_ID).then();

        ApiAssertions.shouldBeOk(response);
        ApiAssertions.shouldHaveProductDetails(response, KNOWN_PRODUCT_ID);
    }

    @Test
    public void shouldReturnNotFoundWhenIdIsInvalid() {
        products.byId(999999).then()
                .statusCode(anyOf(is(404), is(400)));
    }

    @Test(dataProvider = "validProductData", dataProviderClass = TestDataProvider.class)
    public void shouldCreateProductSuccessfully(String baseTitle, double ignoredPrice) {
        ProductRequest request = ProductFactory.randomValidProduct(baseTitle);

        ValidatableResponse response = products.add(request).then();

        ApiAssertions.shouldBeCreated(response);
        ApiAssertions.shouldMatchProductRequest(response, request);
    }

    @Test(dataProvider = "invalidProductData", dataProviderClass = TestDataProvider.class)
    public void shouldDocumentThatApiAcceptsInvalidDataWhenAddingProduct(String title, double price) {
        ProductRequest request = title == null
                ? ProductFactory.productWithNullTitle()
                : ProductFactory.validProduct(title, price);

        ValidatableResponse response = products.add(request).then();

        ApiAssertions.shouldBeCreated(response);
        ApiAssertions.shouldHaveId(response);

        response.body("title", equalTo(request.getTitle()));
        Number returnedPrice = response.extract().path("price");
        org.testng.Assert.assertEquals(returnedPrice.doubleValue(), request.getPrice(), 0.001);
    }

    @Test
    public void shouldDocumentPaginationBehaviorAtBoundaryValues() {
        ValidatableResponse zeroLimitResponse = products.list(0, 0).then();
        ApiAssertions.shouldBeOk(zeroLimitResponse);
        ApiAssertions.shouldHaveNonEmptyArray(zeroLimitResponse, "products");
        zeroLimitResponse.body("skip", equalTo(0))
                         .body("total", greaterThan(0))
                         .body("limit", greaterThanOrEqualTo(0));

        ApiAssertions.shouldBeBadRequestOrOk(products.list(10, -1).then());
    }

    @Test
    public void shouldDocumentBehaviorWhenPriceIsNegative() {
        ProductRequest request = ProductFactory.productWithNegativePrice();
        ValidatableResponse response = products.add(request).then();

        ApiAssertions.shouldBeCreated(response);
        ApiAssertions.shouldHaveId(response);
        response.body("title", equalTo(request.getTitle()))
                .body("price", equalTo((float) request.getPrice()));
    }

    @Test
    public void shouldDocumentBehaviorWhenTitleIsEmpty() {
        ProductRequest request = ProductFactory.productWithEmptyTitle();
        ValidatableResponse response = products.add(request).then();

        ApiAssertions.shouldBeCreated(response);
        response.body("title", isEmptyString());

        Number returnedPrice = response.extract().path("price");
        org.testng.Assert.assertEquals(returnedPrice.doubleValue(), request.getPrice(), 0.001);
    }

    @Test
    public void shouldDocumentApiBehaviorForInvalidPagination() {
        ApiAssertions.shouldBeBadRequestOrOk(products.list(-1, -10).then());
    }

    @Test
    public void shouldEnsureMinimumContractWhenGettingProductById() {
        ValidatableResponse response = products.byId(KNOWN_PRODUCT_ID).then();

        ApiAssertions.shouldBeOk(response);
        ApiAssertions.shouldHaveProductCoreFields(response);
    }

    @Test
    public void shouldReturnUnauthorizedWhenAccessingProtectedEndpointWithoutToken() {
        ValidatableResponse response = products.authProductsWithoutToken().then();

        ApiAssertions.shouldBeUnauthorized(response);
        ApiAssertions.shouldHaveUnauthorizedErrorDetails(response);
    }

    @Test
    public void shouldDocumentProductListingStability() {
        int limit = 5;
        int skip = 0;

        for (int attempt = 0; attempt < 3; attempt++) {
            ValidatableResponse response = products.list(limit, skip).then();

            ApiAssertions.shouldBeOk(response);
            ApiAssertions.shouldHaveNonEmptyArray(response, "products");
            ApiAssertions.shouldHavePaginationMetadata(response, limit, skip);
        }
    }

    @Test
    public void shouldDocumentBehaviorWhenSkipExceedsTotal() {
        ValidatableResponse response = products.list(10, 999999).then();

        ApiAssertions.shouldBeOk(response);
        ApiAssertions.shouldHaveEmptyArray(response, "products");
        response.body("skip", equalTo(999999))
                .body("total", greaterThanOrEqualTo(0))
                .body("limit", greaterThanOrEqualTo(0));
    }
}
