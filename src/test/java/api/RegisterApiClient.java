package api;

import models.auth.AuthRequest;
import models.auth.RegisterResponse;
import models.ErrorResponse;

import static io.restassured.RestAssured.given;
import static specs.ApiSpecs.*;

public class RegisterApiClient {

    public static RegisterResponse register(AuthRequest registerBody) {
        return given(getRegisterSpec())
                .body(registerBody)
                .when()
                .post()
                .then()
                .spec(getSuccessResponseSpec())
                .extract().as(RegisterResponse.class);
    }

    public static ErrorResponse registerError(AuthRequest registerBody) {
        return given(getRegisterSpec())
                .body(registerBody)
                .when()
                .post()
                .then()
                .spec(getBadRequestResponseSpec())
                .extract().as(ErrorResponse.class);
    }
}