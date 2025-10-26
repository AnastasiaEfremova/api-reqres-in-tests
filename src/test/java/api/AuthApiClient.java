package api;

import models.auth.AuthRequest;
import models.auth.LoginResponse;
import models.ErrorResponse;

import static io.restassured.RestAssured.given;
import static specs.ApiSpecs.*;

public class AuthApiClient {

    public static LoginResponse login(AuthRequest loginBody) {
        return given(getAuthSpec())
                .body(loginBody)
                .when()
                .post()
                .then()
                .spec(getSuccessResponseSpec())
                .extract().as(LoginResponse.class);
    }

    public static ErrorResponse loginError(AuthRequest loginBody) {
        return given(getAuthSpec())
                .body(loginBody)
                .when()
                .post()
                .then()
                .spec(getBadRequestResponseSpec())
                .extract().as(ErrorResponse.class);
    }
}