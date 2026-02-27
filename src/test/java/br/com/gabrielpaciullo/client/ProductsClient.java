package br.com.gabrielpaciullo.client;

import br.com.gabrielpaciullo.model.ProductRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class ProductsClient extends BaseClient {

    public ProductsClient(RequestSpecification spec) {
        super(spec);
    }

    public Response list(int limit, int skip) {
        return get("/products", Map.of("limit", limit, "skip", skip));
    }

    public Response byId(int id) {
        return get("/products/" + id);
    }

    public Response add(ProductRequest request) {
        return post("/products/add", request);
    }

    public Response authProducts(String token) {
        return getWithBearer("/auth/products", token);
    }
}