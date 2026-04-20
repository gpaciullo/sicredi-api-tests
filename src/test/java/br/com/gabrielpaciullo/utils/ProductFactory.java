package br.com.gabrielpaciullo.utils;

import br.com.gabrielpaciullo.model.ProductRequest;

public final class ProductFactory {

    private ProductFactory() {
    }

    public static ProductRequest validProduct(String title, double price) {
        return new ProductRequest(title, price);
    }

    public static ProductRequest productWithNegativePrice() {
        return new ProductRequest("Produto inválido QA", -10.0);
    }

    public static ProductRequest productWithEmptyTitle() {
        return new ProductRequest("", 100.0);
    }
}
