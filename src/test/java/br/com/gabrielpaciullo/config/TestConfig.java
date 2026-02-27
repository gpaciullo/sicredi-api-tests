package br.com.gabrielpaciullo.config;

import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
	private final Properties props = new Properties();

	public TestConfig() {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (is != null) {
				props.load(is);
			} else {
				System.out.println("[WARN] config.properties não encontrado no classpath. Usando defaults.");
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
}