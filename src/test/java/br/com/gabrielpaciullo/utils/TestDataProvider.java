package br.com.gabrielpaciullo.utils;

import org.testng.annotations.DataProvider;

public class TestDataProvider {

	@DataProvider(name = "validProductData")
    public Object[][] validProductData() {
        return new Object[][] {
                {"Produto QA", 123.45},
                {"Produto Promo", 9.99},
                {"Produto Premium", 999.90}
        };
    }

    @DataProvider(name = "invalidProductData")
    public Object[][] invalidProductData() {
        return new Object[][] {
                {"", 10.0},              // empty title
                {null, 10.0},            // null title
                {"Produto Negativo", -1} // invalid price
        };
    }
}