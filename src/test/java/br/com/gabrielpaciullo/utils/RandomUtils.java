package br.com.gabrielpaciullo.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomUtils {

    private static final String[] PRODUCT_PREFIXES = {
            "Notebook", "Mouse", "Teclado", "Monitor", "Headset", "Cadeira"
    };

    private static final String[] PRODUCT_SUFFIXES = {
            "QA", "Automation", "Regression", "Smoke", "API", "E2E"
    };

    private RandomUtils() {
    }

    public static String randomProductName() {
        return randomPrefix() + "-" + randomSuffix() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static double randomValidPrice() {
        return roundToTwoDecimals(ThreadLocalRandom.current().nextDouble(5.0, 5000.0));
    }

    public static double randomNegativePrice() {
        return -roundToTwoDecimals(ThreadLocalRandom.current().nextDouble(1.0, 500.0));
    }

    public static String randomPrefix() {
        return PRODUCT_PREFIXES[ThreadLocalRandom.current().nextInt(PRODUCT_PREFIXES.length)];
    }

    public static String randomSuffix() {
        return PRODUCT_SUFFIXES[ThreadLocalRandom.current().nextInt(PRODUCT_SUFFIXES.length)];
    }

    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
