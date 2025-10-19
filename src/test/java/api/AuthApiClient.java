package api;

import models.login.LoginRequest;
import models.login.LoginResponse;

import static io.restassured.RestAssured.given;
import static specs.ApiSpecs.*;

public class AuthApiClient {

    public static LoginResponse login(LoginRequest loginBody) {
        return given(getAuthSpec())
                .body(loginBody)
                .when()
                .post()
                .then()
                .spec(getSuccessResponseSpec())
                .extract().as(LoginResponse.class);
    }

    public static LoginResponse loginWithCredentials(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        return login(request);
    }
}