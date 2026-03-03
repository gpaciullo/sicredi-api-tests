package br.com.gabrielpaciullo.utils;

import java.util.UUID;

public class RandomUtils {

    public static String randomProductName() {
        return "Product-" + UUID.randomUUID();
    }

}