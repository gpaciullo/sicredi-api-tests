package br.com.gabrielpaciullo.utils;

import br.com.gabrielpaciullo.model.ProductRequest;

public class ProductFactory {
    public static ProductRequest produtoValido(String title, double price) {
        return new ProductRequest(title, price);
    }
}