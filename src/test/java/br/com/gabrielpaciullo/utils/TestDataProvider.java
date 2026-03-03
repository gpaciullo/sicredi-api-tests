package br.com.gabrielpaciullo.utils;

import org.testng.annotations.DataProvider;

public class TestDataProvider {

    @DataProvider(name = "productData")
    public Object[][] productData() {
        return new Object[][] {
                {"Produto QA", 123.45},
                {"Produto Promo", 9.99},
                {"Produto Premium", 999.90}
        };
    }

    @DataProvider(name = "invalidProductData")
    public Object[][] invalidProductData() {
        return new Object[][] {
                {"", 10.0},              // title vazio
                {null, 10.0},            // title null
                {"Produto Negativo", -1} // preço inválido
        };
    }
}