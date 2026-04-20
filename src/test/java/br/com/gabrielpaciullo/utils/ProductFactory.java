package br.com.gabrielpaciullo.utils;

import br.com.gabrielpaciullo.model.ProductRequest;

public final class ProductFactory {

    private ProductFactory() {
    }

    public static ProductRequest validProduct(String title, double price) {
        return new ProductRequest(title, price);
    }

    public static ProductRequest randomValidProduct() {
        return new ProductRequest(RandomUtils.randomProductName(), RandomUtils.randomValidPrice());
    }

    public static ProductRequest randomValidProduct(String baseTitle) {
        return new ProductRequest(baseTitle + "-" + RandomUtils.randomProductName(), RandomUtils.randomValidPrice());
    }

    public static ProductRequest productWithNegativePrice() {
        return new ProductRequest(RandomUtils.randomProductName(), RandomUtils.randomNegativePrice());
    }

    public static ProductRequest productWithNegativePrice(String title) {
        return new ProductRequest(title + "-" + RandomUtils.randomProductName(), RandomUtils.randomNegativePrice());
    }

    public static ProductRequest productWithEmptyTitle() {
        return new ProductRequest("", RandomUtils.randomValidPrice());
    }

    public static ProductRequest productWithNullTitle() {
        return new ProductRequest(null, RandomUtils.randomValidPrice());
    }
}
