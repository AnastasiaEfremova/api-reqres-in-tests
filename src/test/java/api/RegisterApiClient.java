package api;

import models.register.RegisterRequest;
import models.register.RegisterResponse;
import models.ErrorResponse;

import static io.restassured.RestAssured.given;
import static specs.ApiSpecs.*;

public class RegisterApiClient {

    public static RegisterResponse register(RegisterRequest registerBody) {
        return given(getRegisterSpec())
                .body(registerBody)
                .when()
                .post()
                .then()
                .spec(getSuccessResponseSpec())
                .extract().as(RegisterResponse.class);
    }

    public static ErrorResponse registerError(RegisterRequest registerBody) {
        return given(getRegisterSpec())
                .body(registerBody)
                .when()
                .post()
                .then()
                .spec(getBadRequestResponseSpec())
                .extract().as(ErrorResponse.class);
    }
}