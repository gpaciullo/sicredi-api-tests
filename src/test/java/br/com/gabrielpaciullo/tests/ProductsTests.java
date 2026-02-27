package br.com.gabrielpaciullo.tests;

import br.com.gabrielpaciullo.client.ProductsClient;
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
	public void deveRetornar404QuandoIdInvalido() {
		products.byId(999999).then()
				// a API pode responder 404 ou 400 dependendo da implementação
				.statusCode(anyOf(is(404), is(400)));
	}

	@Test
	public void deveAdicionarProduto() {
		String title = "Produto QA " + System.currentTimeMillis();

		products.add(new ProductRequest(title, 123.45)).then().statusCode(anyOf(is(200), is(201)))
				.body("id", notNullValue()).body("title", equalTo(title)).body("price", notNullValue());
	}
}