package api;

import models.login.LoginRequest;
import models.login.LoginResponse;
import models.ErrorResponse;

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

    public static ErrorResponse loginError(LoginRequest loginBody) {
        return given(getAuthSpec())
                .body(loginBody)
                .when()
                .post()
                .then()
                .spec(getBadRequestResponseSpec())
                .extract().as(ErrorResponse.class);
    }
}