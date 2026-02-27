package br.com.gabrielpaciullo.client;

import br.com.gabrielpaciullo.model.LoginRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AuthClient extends BaseClient {

    public AuthClient(RequestSpecification spec) {
        super(spec);
    }

    /**
     * POST /auth/login
     */
    public Response login(LoginRequest request) {
        return post("/auth/login", request);
    }
}