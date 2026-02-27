package br.com.gabrielpaciullo.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {

    protected RequestSpecification spec;
    protected TestConfig cfg;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        cfg = new TestConfig();

        RestAssured.baseURI = cfg.baseUrl();
        RestAssured.basePath = cfg.apiBasePath();

        // Loga request/response automaticamente SOMENTE se algum assert falhar
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);

        // Allure integration para RestAssured (sem precisar mexer nos testes)
        Filter allureFilter = new io.qameta.allure.restassured.AllureRestAssured();

        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addFilter(allureFilter)
                .build();
    }
}