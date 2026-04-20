package br.com.gabrielpaciullo.config;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class TestConfig {

    private static final Logger LOG = Logger.getLogger(TestConfig.class.getName());
    private final Properties props = new Properties();

    public TestConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                props.load(is);
            } else {
                LOG.warning("config.properties not found in the classpath. Using defaults.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha ao carregar config.properties", e);
        }
    }

    public String baseUrl() {
        return System.getProperty("baseUrl", props.getProperty("baseUrl", "https://dummyjson.com")).trim();
    }

    public String apiBasePath() {
        return System.getProperty("apiBasePath", props.getProperty("apiBasePath", "")).trim();
    }

    public int timeoutMs() {
        return Integer.parseInt(System.getProperty("timeoutMs", props.getProperty("timeoutMs", "15000")).trim());
    }
}