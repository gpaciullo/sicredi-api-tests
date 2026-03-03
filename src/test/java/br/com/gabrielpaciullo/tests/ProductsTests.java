package br.com.gabrielpaciullo.tests;

import br.com.gabrielpaciullo.client.ProductsClient;
import br.com.gabrielpaciullo.utils.TestDataProvider;
import br.com.gabrielpaciullo.config.BaseTest;
import br.com.gabrielpaciullo.model.ProductRequest;
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
	public void deveListarProdutosComPaginacao() {
		int limit = 10;
		int skip = 0;

		products.list(limit, skip).then().statusCode(200).body("products", is(not(empty())))
				.body("limit", equalTo(limit)).body("skip", equalTo(skip)).body("total", greaterThan(0));
	}

	@Test
	public void deveBuscarProdutoPorId() {
		int id = 1;

		products.byId(id).then().statusCode(200).body("id", equalTo(id)).body("title", not(isEmptyOrNullString()));
	}

	@Test
	public void shouldReturnNotFoundForInvalidProductId() {
		products.byId(999999).then()
				// a API pode responder 404 ou 400 dependendo da implementação
				.statusCode(anyOf(is(404), is(400)));
	}

	@Test(dataProvider = "productData", dataProviderClass = TestDataProvider.class)
	public void shouldCreateProductSuccessfully(String baseTitle, double price) {
	    String title = baseTitle + " " + System.currentTimeMillis();

	    products.add(new ProductRequest(title, price))
	            .then()
	            .statusCode(anyOf(is(200), is(201)))
	            .body("id", notNullValue())
	            .body("title", equalTo(title))
	            .body("price", notNullValue());
	}
	
	@Test(dataProvider = "invalidProductData", dataProviderClass = TestDataProvider.class)
	public void deveDocumentarQueApiAceitaDadosInvalidosAoAdicionarProduto(String title, double price) {

	    products.add(new ProductRequest(title, price))
	            .then()
	            // comportamento atual da API: aceita dados inválidos
	            .statusCode(anyOf(is(200), is(201)))
	            .body("id", notNullValue())
	            .body("price", notNullValue());
	}
}