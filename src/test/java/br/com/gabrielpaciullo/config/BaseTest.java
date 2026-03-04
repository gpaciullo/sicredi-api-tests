package br.com.gabrielpaciullo.config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
import static org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT;

public abstract class BaseTest {

    protected RequestSpecification spec;
    protected TestConfig cfg;

    @BeforeClass(alwaysRun = true)
    public void setup() {

        cfg = new TestConfig();

        enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);

        Filter allureFilter = new AllureRestAssured();

        RestAssuredConfig config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(CONNECTION_TIMEOUT, cfg.timeoutMs())
                        .setParam(SO_TIMEOUT, cfg.timeoutMs()));

        spec = new RequestSpecBuilder()
                .setBaseUri(cfg.baseUrl())
                .setBasePath(cfg.apiBasePath())
                .setContentType(ContentType.JSON)
                .setConfig(config)
                .addFilter(allureFilter)
                .build();
    }
}