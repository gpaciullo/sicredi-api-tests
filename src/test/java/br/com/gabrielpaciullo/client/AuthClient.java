package br.com.gabrielpaciullo.client;

import br.com.gabrielpaciullo.model.LoginRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AuthClient extends BaseClient {

    private static final String AUTH_LOGIN = "/auth/login";
    private static final String DEFAULT_USERNAME = "emilys";
    private static final String DEFAULT_PASSWORD = "emilyspass";

    public AuthClient(RequestSpecification spec) {
        super(spec);
    }

    public Response login(LoginRequest request) {
        return post(AUTH_LOGIN, request);
    }

    public Response login(String username, String password) {
        return login(new LoginRequest(username, password));
    }

    public Response loginWithDefaultCredentials() {
        String username = System.getProperty("username", DEFAULT_USERNAME);
        String password = System.getProperty("password", DEFAULT_PASSWORD);

        return login(username, password);
    }
}
