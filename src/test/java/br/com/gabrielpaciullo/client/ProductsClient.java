package br.com.gabrielpaciullo.client;

import br.com.gabrielpaciullo.model.ProductRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class ProductsClient extends BaseClient {

    private static final String PRODUCTS = "/products";
    private static final String PRODUCTS_ADD = "/products/add";
    private static final String AUTH_PRODUCTS = "/auth/products";

    public ProductsClient(RequestSpecification spec) {
        super(spec);
    }

    public Response list(int limit, int skip) {
        return get(PRODUCTS, Map.of("limit", limit, "skip", skip));
    }

    public Response byId(int id) {
        return get(String.format("%s/%d", PRODUCTS, id));
    }

    public Response add(ProductRequest request) {
        return post(PRODUCTS_ADD, request);
    }

    public Response authProducts(String token) {
        return getWithBearer(AUTH_PRODUCTS, token);
    }

    public Response authProductsWithoutToken() {
        return get(AUTH_PRODUCTS);
    }
}
