package api;

import models.users.CreateUserResponse;
import models.users.UserRequest;
import models.users.UserResponse;
import models.users.UsersResponse;
import models.users.UpdateUserResponse;

import static io.restassured.RestAssured.given;
import static specs.ApiSpecs.*;

public class UsersApiClient {

    public static UsersResponse getUsers(int page) {
        return given(getUsersSpec())
                .param("page", page)
                .when()
                .get()
                .then()
                .spec(getSuccessResponseSpec())
                .extract().as(UsersResponse.class);
    }

    public static UserResponse getUser(int userId) {
        return given(getUsersSpec())
                .pathParam("id", userId)
                .when()
                .get("/{id}")
                .then()
                .spec(getSuccessResponseSpec())
                .extract().as(UserResponse.class);
    }

    public static CreateUserResponse createUser(UserRequest user) {
        return given(getUsersSpec())
                .body(user)
                .when()
                .post()
                .then()
                .spec(getCreatedResponseSpec())
                .extract().as(CreateUserResponse.class);
    }

    public static UpdateUserResponse updateUser(UserRequest user, int userId) {
        return given(getUsersSpec())
                .pathParam("id", userId)
                .body(user)
                .when()
                .put("/{id}")
                .then()
                .spec(getSuccessResponseSpec())
                .extract().as(UpdateUserResponse.class);
    }

    public static UpdateUserResponse patchUser(UserRequest user, int userId) {
        return given(getUsersSpec())
                .pathParam("id", userId)
                .body(user)
                .when()
                .patch("/{id}")
                .then()
                .spec(getSuccessResponseSpec())
                .extract().as(UpdateUserResponse.class);
    }

    public static void deleteUser(int userId) {
        given(getUsersSpec())
                .pathParam("id", userId)
                .when()
                .delete("/{id}")
                .then()
                .spec(getNoContentResponseSpec());
    }
}